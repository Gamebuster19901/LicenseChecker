package com.gamebuster19901.license.flags;

import java.io.File;
import java.io.FileNotFoundException;

import com.gamebuster19901.license.NotFileException;
import com.gamebuster19901.license.create.CheckerSettings;
import com.gamebuster19901.license.flags.HeaderFlag.LocationFlag;
import com.google.common.io.Files;

public final class FileFlag extends LocationFlag {
	
	public FileFlag() {
		super("file", "will read the license text from a file", 0);
		this.exclude("string");
	}

	public byte[] getHeader(CheckerSettings settings, String location) throws Exception{
		byte[] header;
		File licenseFile = new File(settings.getMessage(location));
		header = new byte[(int)licenseFile.length()];
		Files.asByteSource(licenseFile).openStream().read(header, 0, (int)licenseFile.length());
		return header;
	}

	@Override
	public void validate(CheckerSettings settings, String extension, String value) throws Exception {
		File f = new File(value);
		if(!f.exists()) {
			throw new FileNotFoundException("Could not find license file for extension " + extension + "\n\n" + f.getAbsolutePath());
		}
		if(f.isDirectory()) {
			throw new NotFileException(f.getAbsolutePath());
		}
	}
	
}
