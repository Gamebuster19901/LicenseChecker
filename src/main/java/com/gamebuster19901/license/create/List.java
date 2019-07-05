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

public class List extends Command{

	public List() {
		super("/list", "[<boolean:asJson>]", "prints the list of extensions", "prints the list of extensions");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		if(params.isEmpty() || params.equals("false")) {
			String[] extensions = CreateChecker.getSettings().getExtensions();
			String[] messages = CreateChecker.getSettings().getMessages();
			System.out.println("==========================");
			for(int i = 0; i < extensions.length; i++) {
				System.out.println(extensions[i]);
				System.out.println(messages[i]);
				System.out.println();
			}
			System.out.println("==========================");
		}
		else if(params.equals("true")) {
			System.out.println(CreateChecker.GSON.toJson(CreateChecker.checkerSettings));
		}
		else {
			new IllegalArgumentException(params).printStackTrace();
		}
	}
	
}
