package com.gamebuster19901.license.flags;

import java.io.File;

import com.gamebuster19901.license.LicenseChecker;
import com.gamebuster19901.license.create.CheckerSettings;
import com.google.common.io.Files;

public class AppendFlag extends HeaderFlag.DataTypeFlag{

	public AppendFlag() {
		super("append", "appends the header to the top of the file", 0);
		exclude("json");
	}
	
	@Override
	public void apply(File f) throws Exception {
		String extension = getExtension(f);
		byte[] headerBytes;
		byte[] fileBytes;
		
		headerBytes = LicenseChecker.getLicense(extension);
		fileBytes = new byte[headerBytes.length + (int)f.length()];
		for(int i = 0; i < headerBytes.length; i++) {
			fileBytes[i] = headerBytes[i];
		}
		
		Files.asByteSource(f).openStream().read(fileBytes, headerBytes.length, (int)f.length());
		Files.asByteSink(f).write(fileBytes);
	}

	@Override
	public void validate(CheckerSettings settings, String extension, String value) throws Exception {}
}
