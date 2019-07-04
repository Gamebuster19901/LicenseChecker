package com.gamebuster19901.license.flags;

import com.gamebuster19901.license.create.CheckerSettings;
import com.gamebuster19901.license.flags.HeaderFlag.LocationFlag;

public class StringFlag extends LocationFlag{
	
	public StringFlag() {
		super("string", "will read the license text from the raw string", 0);
		exclude("file");
	}

	@Override
	public byte[] getHeader(CheckerSettings settings, String extension) throws Exception {
		return settings.getMessage(extension).getBytes();
	}

	@Override
	public void validate(CheckerSettings settings, String extension, String value) throws Exception {
		if(value.trim().isEmpty()) {
			throw new RuntimeException("Empty header detected for extension: " + extension);
		}
	}

}
