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

package com.gamebuster19901.license.create;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.io.Files;
import com.google.gson.JsonParseException;

public class Import extends Command{

	public Import(String command, String params, String shortDescription) {
		super(command, params, shortDescription);
		// TODO Auto-generated constructor stub
	}

	public Import() {
		super("/import", "[<string:fullPathToFile>]", "Imports license headers from a json file");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		try {
			File importLoc = Export.EXPORT_LOCATION;
			if(!params.isEmpty()) {
				importLoc = new File(params);
			}
			String json = Files.asCharSource(importLoc, Charset.defaultCharset()).read();
			CreateChecker.checkerSettings = CreateChecker.GSON.fromJson(json, CheckerSettings.class);
		}
		catch(IOException | JsonParseException e) {
			e.printStackTrace();
			System.err.println("could not read json file");
		}
	}
	
}
