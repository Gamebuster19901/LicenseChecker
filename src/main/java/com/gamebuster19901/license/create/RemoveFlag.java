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

import com.gamebuster19901.license.flags.HeaderFlag;

public class RemoveFlag extends Command{

	public RemoveFlag() {
		super("/removeflag", "<string:flag>", "removes an edit flag for the current header");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		if(CreateChecker.checkerSettings.getExtension() != null) {
			for(HeaderFlag flag : HeaderFlag.getEditFlags()) {
				if(params.equalsIgnoreCase(flag.name())) {
					CreateChecker.checkerSettings.currentMode.remove(flag);
					return;
				}
			}
			System.out.println(params + " is not a valid flag!");
		}
		else {
			System.out.println("You must be editing a header to edit its flag!");
		}
	}

}
