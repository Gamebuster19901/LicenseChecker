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

public class RemoveMode extends Command{

	public RemoveMode() {
		super("/removemode", "<append|insert|file|string>", "removes a header mode for the current header, note: will use defaults if none specified",
				"removes a header mode for the current header, note: will use defaults if none specified\n"
				+ "\n"
				+ "The default values are [append, string]\n"
				+ "if append or insert is not specified, it will default to append\n"
				+ "if file or string is not specified, it will default to file\n");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		if(CreateChecker.checkerSettings.getExtension() != null) {
			for(HeaderMode mode : HeaderMode.values()) {
				if(params.equalsIgnoreCase(mode.name())) {
					CreateChecker.checkerSettings.currentMode.remove(mode);
					return;
				}
			}
			System.out.println(params + " is not a valid mode!");
		}
		else {
			System.out.println("You must be editing a header to edit its mode!");
		}
	}

}
