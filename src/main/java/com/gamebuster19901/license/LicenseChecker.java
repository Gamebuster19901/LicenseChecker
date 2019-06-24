package com.gamebuster19901.license;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LicenseChecker {

	public static void main(String[] args) {
		try {
			Path path = Paths.get(LicenseChecker.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			if(args.length == 1) {
				path = FileSystems.getDefault().getPath(args[0]).toAbsolutePath();
			}
			
			File dir = path.toFile();
			for(File f : dir.listFiles()) {
				//TODO: check licenses
			}
		}
		catch(LicenseException e) {
			e.printStackTrace();
			System.exit(2);
		}
		catch(Throwable t) {
			t.printStackTrace();
			System.exit(3);
		}
	}
	
}
