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
