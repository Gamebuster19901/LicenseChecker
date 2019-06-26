package com.gamebuster19901.license;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

import com.gamebuster19901.license.create.CheckerSettings;
import com.google.common.io.Files;
import com.google.gson.Gson;

public class LicenseChecker {

	public static boolean silentSkips = false;
	public static boolean silentIgnores = false;
	
	public static void main(String[] args) {
		for(String s : args) {
			if(s.equalsIgnoreCase("silenceSkips")){
				silentSkips = true;
			}
			else if (s.equalsIgnoreCase("silenceIgnores")) {
				silentIgnores = true;
			}
		}
		try {
			String path = Paths.get(LicenseChecker.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
			if(path.endsWith(".jar")) {
				File dir = new File(path);
				path = dir.getParent();
			}
			if(args.length == 1) {
				path = FileSystems.getDefault().getPath(args[0]).toAbsolutePath().toString();
			}
			File dir = new File(path);
			
			System.out.println("Looking for licenses.json...\n");
			File licensesJSON = new File(path + "/licenses.json");
			if(licensesJSON.exists()) {
				System.out.println("Found licences.json\n");
				CheckerSettings settings = validateLicenseJson(licensesJSON);
				System.out.println("Checking for license violations in: " + path + "\n");
				
				HashSet<File> badFiles = new HashSet<File>();
				
				for(File f : Files.fileTreeTraverser().breadthFirstTraversal(dir)) {
					if(settings.isIncluded(f)) {
						if(settings.hasExtension("." + Files.getFileExtension(f.getName()))){
							String extension = "." + Files.getFileExtension(f.getName());
							byte[] header = settings.getMessage(extension).getBytes();
							byte[] fileHeader = new byte[header.length];
							Files.asByteSource(f).openStream().read(fileHeader, 0, header.length);
							if(Arrays.equals(header, fileHeader)) {
								System.out.println(f + " looks good");
							}
							else{
								System.err.println(f + " is missing or has an incorrect license");
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
				if(!silentSkips) {
					System.out.println("\nNote: Excluded the following directories/files:\n");
					for(String s : settings.getExclusions()) {
						System.out.println(new File(s).getAbsolutePath());
					}
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
	
	private static CheckerSettings validateLicenseJson(File file) throws Exception {
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
