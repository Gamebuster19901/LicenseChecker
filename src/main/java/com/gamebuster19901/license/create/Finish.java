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

public class Finish extends Command{

	public Finish() {
		super("/finish", "finishes adding the current extension");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		CheckerSettings settings = CreateChecker.getSettings();
		String extension = settings.getExtension();
		String header = settings.getMessage();
		if(extension != null) {
			if(header != null) {
				settings.finishExtension();
			}
			else {
				System.out.println("Could not finish, there was no header provided!");
			}
		}
		else {
			System.out.println("No extension was being added!");
		}
	}

}
