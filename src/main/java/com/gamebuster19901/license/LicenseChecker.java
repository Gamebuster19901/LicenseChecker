/*
 * Copyright 2019 Gamebuster19901
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

import static com.gamebuster19901.license.create.HeaderMode.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.gamebuster19901.license.create.CheckerSettings;
import com.gamebuster19901.license.create.HeaderModes;
import com.google.common.io.Files;
import com.google.gson.Gson;

public class LicenseChecker {

	public static boolean silentSkips = false;
	public static boolean silentIgnores = false;
	public static boolean applyLicenses = false;
	public static boolean stripLicenses = false;
	private static String path = "";
	
	private static final HashMap<String , byte[]> LICENSES = new HashMap<String, byte[]>();
	
	public static void main(String[] args) {
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
			if(applyLicenses && stripLicenses) {
				throw new IllegalStateException("Cannot both apply and strip licenses at the same time!");
			}
		}
		try {
			String path = Paths.get(LicenseChecker.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
			File dir = new File(path);
			path = dir.getParent();
			if(!LicenseChecker.path.isEmpty()) {
				path = FileSystems.getDefault().getPath(LicenseChecker.path).toAbsolutePath().toString();
			}
			dir = new File(path);
			
			System.out.println("Looking for licenses.json...\n");
			File licensesJSON = new File(path + "/licenseChecker.settings");
			if(licensesJSON.exists()) {
				System.out.println("Found licences.json\n");
				CheckerSettings settings = validate(licensesJSON);
				System.out.println("Checking for license violations in: " + path + "\n");
				
				HashSet<File> badFiles = new HashSet<File>();
				
				for(String extension : settings.getExtensions()) {
					HeaderModes mode = settings.getMode(extension);
					byte[] header;
					if(mode.is(STRING)) {
						header = settings.getMessage(extension).getBytes();
					}
					else if (mode.is(FILE)) {
						File licenseFile = new File(settings.getMessage(extension));
						header = new byte[(int)licenseFile.length()];
						Files.asByteSource(licenseFile).openStream().read(header, 0, (int)licenseFile.length());
					}
					else {
						throw new AssertionError();
					}
					LICENSES.put(extension, header);
				}
				
				for(File f : Files.fileTreeTraverser().breadthFirstTraversal(dir)) {
					if(settings.isIncluded(f)) {
						if(settings.hasExtension("." + Files.getFileExtension(f.getName()))){
							String extension = "." + Files.getFileExtension(f.getName());
							
							byte[] header = LICENSES.get(extension);
							byte[] fileHeader;
							
							if(header == null) {
								throw new AssertionError(new NullPointerException("LICENSES.get(String[" + extension + "]);"));
							}
							
							fileHeader = new byte[header.length];
							Files.asByteSource(f).openStream().read(fileHeader, 0, header.length);
							if(Arrays.equals(header, fileHeader)) {
								System.out.println(f + " looks good");
							}
							else{
								if(!applyLicenses) {
									System.err.println(f + " is missing or has an incorrect license");
								}
								else {
									System.out.println("marking " + f + " for licensing");
								}
								badFiles.add(f);
							}
						}
						else if (!silentSkips){
							System.out.println("Skipping " + f);
						}
					}
					else if (!silentIgnores){
						System.out.println("Ignoring excluded file " + f);
					}
				}
				System.out.println();
				if(!silentSkips) {
					System.out.println("\nNote: Excluded the following directories/files:\n");
					for(String s : settings.getExclusions()) {
						System.out.println(new File(s).getAbsolutePath());
					}
				}
				if(applyLicenses) {
					int badTotal = badFiles.size();
					int fixedFiles = 0;
					for(Iterator<File> iterator = badFiles.iterator(); iterator.hasNext();) {
						File f = iterator.next();
						try {
							String extension = "." + Files.getFileExtension(f.getAbsolutePath());
							System.out.println("adding license header to " + f);
							byte[] headerBytes;
							byte[] fileBytes;
							HeaderModes mode = settings.getMode(extension);
							if(mode.is(APPEND)){
								headerBytes = LICENSES.get(extension);
								fileBytes = new byte[headerBytes.length + (int)f.length()];
								for(int i = 0; i < headerBytes.length; i++) {
									fileBytes[i] = headerBytes[i];
								}
							}
							else if (mode.is(JSON)) {
								headerBytes = LICENSES.get(extension);
								fileBytes = new byte[headerBytes.length + (int)f.length()];
								if(fileBytes.length < 2) {
									fileBytes = new byte[2];
									System.err.println("The file output was less than 2 bytes, a JSON file must be at least 2 bytes, manually setting the length to 2!\n");
								}
								fileBytes[0] = '{';
								fileBytes[1] = '\n';
								for(int i = 2; i < headerBytes.length; i++) {
									fileBytes[i] = headerBytes[i];
								}
							}
							else {
								throw new AssertionError();
							}
							Files.asByteSource(f).openStream().read(fileBytes, headerBytes.length, (int)f.length());
							Files.asByteSink(f).write(fileBytes);
							iterator.remove();
							fixedFiles++;
						}
						catch(Throwable t) {
							System.err.println("Could not license " + f.getAbsolutePath() + "\n");
							t.printStackTrace(System.err);
							continue;
						}
					}
					System.out.println("Licensed (" + fixedFiles + "/" + badTotal + ") licensable files\n");
				}
				if(badFiles.size() > 0) {
					System.err.println();
					throw new LicenseException(badFiles, "The following files have incorrect licensing:\n");
				}
			}
			else {
				throw new LicenseException(licensesJSON, new FileNotFoundException(licensesJSON.getAbsolutePath()));
			}
		}
		catch(LicenseException e) {
			e.printStackTrace(System.err);
			System.exit(2);
		}
		catch(Throwable t) {
			t.printStackTrace(System.err);
			System.exit(3);
		}
		System.out.println("Everything looks good!\n");
	}
	
	private static CheckerSettings validate(File file) throws Exception {
		System.out.println("Validiating licenses.json...\n");
		String json = Files.asCharSource(file, Charset.defaultCharset()).read();
		Gson gson = new Gson();
		CheckerSettings settings = gson.fromJson(json, CheckerSettings.class);
		settings.validate();
		System.out.println("Licenses.json looks good\n");
		System.out.println("Found the following extensions: ");
		System.out.println(Arrays.toString(settings.getExtensions()) + "\n");
		return settings;
	}
	
}
