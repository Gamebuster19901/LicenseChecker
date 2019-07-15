/*
 * LicenseChecker Copyright 2019 Gamebuster19901
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.gamebuster19901.license;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static com.gamebuster19901.license.CheckerPhase.*;
import static com.gamebuster19901.license.flags.HeaderFlag.*;
import static com.gamebuster19901.license.CheckStatus.*;

import com.gamebuster19901.license.create.CheckerSettings;
import com.gamebuster19901.license.flags.HeaderFlag;
import com.gamebuster19901.license.flags.HeaderFlags;
import com.gamebuster19901.license.flags.HeaderFlag.LocationFlag;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class LicenseChecker {

	private static boolean silentSkips = false;
	private static boolean silentIgnores = false;
	private static boolean applyLicenses = false;
	private static boolean stripLicenses = false;
	private static String path = "";
	
	private static final HashMap<String , byte[]> LICENSES = new HashMap<String, byte[]>();
	private static CheckerPhase PHASE = UNINITIALIZED;
	
	public static void main(String[] args) {
		setPhase(INITIALIZING);
		try {
			for(String s : args) {
				if(s.equalsIgnoreCase("silenceSkips")){
					silentSkips = true;
				}
				else if (s.equalsIgnoreCase("silenceIgnores")) {
					silentIgnores = true;
				}
				else if(s.startsWith("path:")) {
					path = s.substring(s.indexOf("path:"), s.length());
				}
				else if (s.equalsIgnoreCase("applyLicenses")) {
					applyLicenses = true;
				}
				else if (s.equalsIgnoreCase("stripLicenses")) {
					stripLicenses = true;
				}
				else if (s.equalsIgnoreCase("notBinary")) {
					path = new File(new File(".").getAbsolutePath()).getParentFile().getAbsolutePath();
				}
				else if (s.startsWith("path:")) {
					path = s.substring(s.indexOf("path:"));
				}
				else if (s.startsWith("classes:")) {
					String classList = s.replace("classes:", "");
					String[] classes = classList.split(",");
					for(String className : classes) {
						LicenseChecker.class.getClassLoader().setPackageAssertionStatus(className.substring(0, className.lastIndexOf('.')), true);
						Class clazz = Class.forName(className);
						Method main = clazz.getDeclaredMethod("main", String[].class);
						main.setAccessible(true);
						main.invoke(null, (Object)args);
					}
				}
				if(applyLicenses && stripLicenses) {
					throw new IllegalStateException("Cannot both apply and strip licenses at the same time!");
				}
			}
			setPhase(INITIALIZED);
			String path = Paths.get(LicenseChecker.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
			File dir = new File(path);
			path = dir.getParent();
			if(!LicenseChecker.path.isEmpty()) {
				path = LicenseChecker.path;
			}
			dir = new File(path);
			
			setPhase(FINDING_SETTINGS);
			System.out.println("Looking for licenseChecker.settings...\n");
			File licensesJSON = new File(path + "/licenseChecker.settings");
			if(licensesJSON.exists()) {
				System.out.println("Found licencesChecker.settings\n");
				CheckerSettings settings = validate(licensesJSON);
				
				HashSet<File> badFiles;
				setLicenses(settings);
				
				if(!stripLicenses) {
					badFiles = check(dir, settings);
					if(applyLicenses) {
						badFiles = apply(settings, badFiles);
					}

					if(badFiles.size() > 0) {
						System.err.println();
						throw new LicenseException(badFiles, "The following files have incorrect licensing:\n");
					}
				}
				else {
					HashMap<File, String> fails = stripLicenses(dir, settings);
					if(fails.size() > 0) {
						throw new LicenseException(fails.keySet(), "Failed to strip some files, see log for more details");
					}
				}
			}
			else {
				if(stripLicenses) {
					System.out.println("Did not strip licenses, no licenseChecker.settings");
					System.exit(0);
				}
				throw new LicenseException(licensesJSON, new FileNotFoundException(licensesJSON.getCanonicalPath()));
			}
		}
		catch(LicenseException e) {
			System.err.println("Failed in phase: " + PHASE);
			e.printStackTrace(System.err);
			setPhase(CheckerPhase.FAILED);
			System.exit(2);
		}
		catch(Throwable t) {
			System.err.println("Failed in phase: " + PHASE);
			t.printStackTrace(System.err);
			setPhase(CheckerPhase.FAILED);
			System.exit(3);
		}
		setPhase(CheckerPhase.SUCCESS);
		System.out.println("Everything looks good!\n");
	}
	
	private static void setLicenses(CheckerSettings settings) throws Exception {
		setPhase(GETTING_HEADERS);
		for(String extension : settings.getExtensions()) {
			HeaderFlags flags = settings.getMode(extension);
			byte[] header;
			for(HeaderFlag flag : flags) {
				if(flag instanceof LocationFlag) {
					header = ((LocationFlag) flag).getHeader(settings, extension);
					if (LICENSES.put(extension, header) != null) {
						throw new AssertionError("Header already set?!");
					}
				}
			}
		}
	}
	
	private static HashSet<File> check(File dir, CheckerSettings settings) throws Exception{
		setPhase(CHECKING_HEADERS);
		System.out.println("Checking for license violations in: " + path + "\n");
		HashSet<File> badFiles = new HashSet<File>();
		for(File f : Files.fileTraverser().breadthFirst(dir)) {
			CheckStatus status = getStatus(f, settings);
			switch (status) {
				case PASSED:
					System.out.println(f + " looks good");
					break;
				case FAILED:
					if(!applyLicenses) {
						System.err.println(f + " is missing or has an incorrect license");
					}
					else {
						System.out.println("marking " + f + " for licensing");
					}
					badFiles.add(f);
					break;
				case IGNORED:
					if(!silentIgnores) {
						System.out.println("Ignoring " + f);
					}
					break;
				case SKIPPED:
					if(!silentSkips) {
						System.out.println("Skipping excluded file " + f);
					}
					break;
				default:
					throw new AssertionError(status);
			}
		}
		System.out.println();
		if(!silentSkips) {
			System.out.println("\nNote: Excluded the following directories/files:\n");
			for(String s : settings.getExclusions()) {
				System.out.println(new File(s).getCanonicalPath());
			}
		}
		return badFiles;
	}
	
	private static HashSet<File> apply(CheckerSettings settings, HashSet<File> badFiles) throws Exception {
		setPhase(APPLYING_HEADERS);
		int badTotal = badFiles.size();
		int fixedFiles = 0;
		for(Iterator<File> iterator = badFiles.iterator(); iterator.hasNext();) {
			File f = iterator.next();
			try {
				String extension = getExtension(f);
				HeaderFlags flags = settings.getMode(extension);
				System.out.println("adding license header to " + f);
				for(HeaderFlag flag : flags) {
					if(flag instanceof HeaderFlag.DataTypeFlag) {
						((DataTypeFlag) flag).apply(f);
					}
				}
				iterator.remove();
				fixedFiles++;
			}
			catch(Throwable t) {
				System.err.println("Could not license " + f.getCanonicalPath() + "\n");
				t.printStackTrace(System.err);
				continue;
			}
		}
		System.out.println("Licensed (" + fixedFiles + "/" + badTotal + ") licensable files\n");
		return badFiles;
	}
	
	private static HashMap<File, String> stripLicenses(File dir, CheckerSettings settings) throws Exception {
		setPhase(STRIPPING_HEADERS);
		HashMap<File, String> failedStrippings = new HashMap<File, String>();
		int totalStrippings = 0;
		for(File f : Files.fileTraverser().breadthFirst(dir)) {
			switch(getStatus(f, settings)) {
				case PASSED:
					totalStrippings++;
					try {
						HeaderFlags flags = settings.getMode(getExtension(f));
						for(HeaderFlag flag : flags) {
							if(flag instanceof HeaderFlag.DataTypeFlag) {
								((HeaderFlag.DataTypeFlag) flag).strip(f);
							}
						}
						System.out.println("Stripped " + f);
					}
					catch(Exception e) {
						IOException exception = new IOException("Could not strip license from " + f.getAbsolutePath(), e);
						failedStrippings.put(f, Throwables.getStackTraceAsString(exception));
						exception.printStackTrace(System.err);
					}
					break;
				case FAILED:
					totalStrippings++;
					String message = "Could not find a matching license header in file: " + f.getAbsolutePath();
					failedStrippings.put(f, message);
					System.out.println(message);
					break;
				case IGNORED:
					if(!silentIgnores) {
						System.out.println("Ignoring file with no applicable license header: " + f);
					}
					break;
				case SKIPPED:
					if(!silentSkips) {
						System.out.println("Skipping " + f);
					}
					break;
			}
		}
		for(String s : failedStrippings.values()) {
			System.out.println(s);
		}
		System.out.println("\nStripped (" + (totalStrippings - failedStrippings.size()) + "/" + totalStrippings + ") applicable files\n");
		return failedStrippings;
	}
	
	static CheckStatus getStatus(File file, CheckerSettings settings) throws Exception{
		if(!settings.isIncluded(file)) {
			return SKIPPED;
		}
		String extension = getExtension(file);
		if(settings.hasExtension(extension)){
			byte[] header = LICENSES.get(extension);
			byte[] fileHeader;
			
			if(header == null) {
				throw new AssertionError(new NullPointerException("LICENSES.get(String[" + extension + "]);"));
			}
			
			fileHeader = new byte[header.length];
			Files.asByteSource(file).openStream().read(fileHeader, 0, header.length);
			if (Arrays.equals(header, fileHeader)){
				return PASSED;
			}
			return CheckStatus.FAILED;
		}
		return IGNORED;
	}
	
	private static CheckerSettings validate(File file) throws Exception {
		setPhase(VALIDATING_SETTINGS);
		System.out.println("Validiating licenseChecker.settings...\n");
		String json = Files.asCharSource(file, Charset.defaultCharset()).read();
		Gson gson = new GsonBuilder().registerTypeAdapter(HeaderFlag.class, HeaderFlag.DESERIALIZER).create();
		CheckerSettings settings = gson.fromJson(json, CheckerSettings.class);
		settings.validate();
		System.out.println("licenseChecker.settings looks good\n");
		System.out.println("Found the following extensions: ");
		System.out.println(Arrays.toString(settings.getExtensions()) + "\n");
		return settings;
	}
	
	public static final byte[] getLicense(String extension) {
		return LICENSES.get(extension);
	}

	public static final boolean isSilentlySkipping() {
		return silentSkips;
	}

	public static final boolean isSilentlyIgnoring() {
		return silentIgnores;
	}

	public static final boolean isApplyingLicenses() {
		return applyLicenses;
	}

	public static final boolean isStrippingLicenses() {
		return stripLicenses;
	}

	public static final CheckerPhase getPhase() {
		return PHASE;
	}
	
	private static void setPhase(CheckerPhase phase) {
		PHASE = phase;
	}
	
}
