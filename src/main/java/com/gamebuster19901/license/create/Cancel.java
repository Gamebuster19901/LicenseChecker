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

public class Cancel extends Command{

	public Cancel() {
		super("/cancel", "Cancels the extension you're adding without saving");
	}

	@Override
	public void exec(String params) {
		CheckerSettings settings = CreateChecker.getSettings();
		String extension = settings.getExtension();
		if(extension != null) {
			settings.clear();
			System.out.println("Cancelled the addition of '" + extension + "'");
		}
		else {
			System.out.println("No extension was being added!");
		}
	}
	
}
