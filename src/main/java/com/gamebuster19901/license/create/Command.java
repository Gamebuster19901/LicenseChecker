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

import java.util.TreeSet;

@SuppressWarnings("unused")
public abstract class Command implements Comparable{

	private static final TreeSet<Command> COMMANDS = new TreeSet<Command>();
	static {
		new Help();
		new Cancel();
		new Add();
		new Finish();
		new Export();
		new Import();
		new List();
		new Remove();
		new Edit();
		new Undo();
		new AddMode();
		new RemoveMode();
		new Exclude();
		new RemoveExclude();
		new Exit();
	}
	
	public final String command;
	public final String params;
	public final String shortDescription;
	public final String longDescription;
	
	public Command(String command, String shortDescription) {
		this(command, "", shortDescription, shortDescription);
	}
	
	public Command(String command, String params, String shortDescription) {
		this(command, params, shortDescription, shortDescription);
	}
	
	public Command(String command, String params, String shortDescription, String longDescription) {
		this.command = command;
		this.params = params;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		if(!COMMANDS.add(this)) {
			throw new IllegalStateException();
		}
	}
	
	public abstract void exec(String params) throws InterruptedException;
	
	public final boolean matches(String input) {
		if(input.indexOf(' ') != -1) {
			input = input.substring(0, input.indexOf(' '));
		}
		String escapedInput = "^" + escapeRegex(input) + "(\\s.*|\\s$|\\n$|$)";
		if(command.matches(escapedInput)) {
			return true;
		}
		return false;
	}
	
	private final String escapeRegex(String input) {
		input = input.replaceAll("[\\W]", "\\\\$0");
		return input;
	}
	
	public String toString() {
		if(params.isEmpty()) {
			return command + ": " + shortDescription;
		}
		else {
			return command + " " + params + ": " + shortDescription;
		}
	}
	
	public static Command[] getCommands() {
		return COMMANDS.toArray(new Command[]{});
	}

	@Override
	public final int compareTo(Object arg0) {
		if(arg0 instanceof Command) {
			return command.compareTo(((Command) arg0).command);
		}
		throw new IllegalStateException();
	}
}
