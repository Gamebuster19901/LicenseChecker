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
