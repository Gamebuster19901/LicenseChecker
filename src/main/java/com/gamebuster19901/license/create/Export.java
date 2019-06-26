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
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

public class Export extends Command{

	public static final File EXPORT_LOCATION;
	static{
		try {
			EXPORT_LOCATION = new File(new File(Export.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().toString() + "/licenseChecker.settings");
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		}
	}
	
	public Export() {
		super("/export", "[<string:fullPathToFile>]", "Exports all license headers to a json file");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		try {
			File exportLoc = EXPORT_LOCATION;
			if(!params.isEmpty()) {
				exportLoc = new File(params);
			}
			String json = CreateChecker.GSON.toJson(CreateChecker.getSettings());
			exportLoc.delete();
			exportLoc.getParentFile().mkdirs();
			exportLoc.createNewFile();
			Files.asCharSink(exportLoc, Charset.defaultCharset(), FileWriteMode.APPEND).write(json);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
