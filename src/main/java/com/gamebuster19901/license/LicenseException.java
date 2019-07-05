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
import java.util.Collection;

@SuppressWarnings("serial")
public class LicenseException extends Exception {

	public static final String DEFAULT_MESSAGE = "The following files are not licensed correctly:\n\n$1";
	
	public LicenseException(Collection<File> files) {
		this(files, DEFAULT_MESSAGE);
	}
	
	public LicenseException(Collection<File> files, String message) {
		super(message + "\n" + listFiles(files));
	}
	
	public LicenseException(File file, Throwable cause) {
		super(file.getAbsolutePath(), cause);
	}
	
	private static String listFiles(Collection<File> files) {
		String message = "";
		for(File f : files) {
			message += f;
			message += '\n';
		}
		return message;
	}
	
}
