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

	@Override
	public void strip(File f) throws Exception {
		byte[] fileBytes = new byte[(int) f.length()]; 
		Files.asByteSource(f).openStream().read(fileBytes, 0, (int)f.length());
		String fileText = new String(fileBytes);
		String license = new String(LicenseChecker.getLicense(getExtension(f)));
		String strippedFile = fileText.substring(license.length());
		Files.asByteSink(f).openStream().write(strippedFile.getBytes());
	}
}
