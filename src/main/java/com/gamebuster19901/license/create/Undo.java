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

package com.gamebuster19901.license.create;

public class Undo extends Command{

	public Undo() {
		super("/undo", "removes the last line from the current license you're editing");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		CheckerSettings settings = CreateChecker.checkerSettings;
		if(settings.getExtension() != null) {
			int index = settings.currentMessage.lastIndexOf("\n");
			if(index == -1) {
				settings.currentMessage = "";
			}
			else {
				settings.currentMessage = settings.currentMessage.substring(0, index);
				index = settings.currentMessage.lastIndexOf("\n");
				if(index == -1) {
					settings.currentMessage = "";
					return;
				}
				settings.currentMessage = settings.currentMessage.substring(0, index);
				settings.addMessage("");
			}
			System.out.println("Current header:");
			System.out.println(settings.getMessage());
		}
		else {
			System.out.println("Not editing, noting to undo!");
		}
	}

}
