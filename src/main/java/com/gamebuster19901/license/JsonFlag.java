package com.gamebuster19901.license;

import java.io.File;

import com.gamebuster19901.license.create.CheckerSettings;
import com.gamebuster19901.license.flags.HeaderFlag;
import com.google.common.io.Files;

public class JsonFlag extends HeaderFlag.DataTypeFlag{

	public JsonFlag() {
		super("json", "will insert the header 1 line below the top of the file (used for json)", 1);
		exclude("append");
	}

	@Override
	public void apply(File f) throws Exception {
		String extension = getExtension(f);
		byte[] headerBytes;
		byte[] fileBytes;
		
		headerBytes = LicenseChecker.getLicense(extension);
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
		
		Files.asByteSource(f).openStream().read(fileBytes, headerBytes.length, (int)f.length());
		Files.asByteSink(f).write(fileBytes);
	}

	@Override
	public void validate(CheckerSettings settings, String extension, String value) throws Exception {}
	
}
