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

public class AddMode extends Command{

	public AddMode() {
		super("/addmode", "<append|json|file|string>", "adds a header mode for the current header", 
				"adds the header mode for the current header\n\n"
				+ "can be either 'append, 'json', 'file' or 'string'\n\n"
						
				+ "append appends the header to the top of the file\n"
				+ "json will insert the header 1 line below the top of the file (used for json)\n"
				+ "if neither append nor json are specified, it will default to append"
				
				+ "file will read the license text from a file instead of the raw string"
				+ "string will read the license text from the raw string"
				+ "if neither file nor string is specified, it will default to string"
				+ ""
				+ "append and json are mutually exclusive"
				+ "file and string are mutually exclusive");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		if(CreateChecker.checkerSettings.getExtension() != null) {
			for(HeaderMode mode : HeaderMode.values()) {
				if(params.equalsIgnoreCase(mode.name())) {
					CreateChecker.checkerSettings.currentMode.add(mode);
					return;
				}
			}
			System.out.println(params + " is not a valid mode!");
		}
		else {
			System.out.println("You must be editing a header to set its mode!");
		}
	}

}
