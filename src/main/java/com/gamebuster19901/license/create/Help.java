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

public class Help extends Command{

	public Help() {
		super("/help", "[<string:command>]", "prints this help prompt, or details on how to use a command");
	}

	@Override
	public void exec(String params) {
		if(params.isEmpty()) {
			System.out.println("===============/help===============");
			for(Command c : Command.getCommands()) {
				System.out.println(c);
			}
			System.out.println("===================================");
			return;
		}
		else {
			for(Command c : Command.getCommands()) {
				if (params.equals(c.command.substring(1, c.command.length()))) {
					System.out.println(c.longDescription);
					return;
				}
			}
			System.out.println("Could not find command: " + params);
		}
	}
}
