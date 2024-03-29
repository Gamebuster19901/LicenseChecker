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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.gamebuster19901.license.NotFileException;
import com.gamebuster19901.license.flags.FileFlag;
import com.gamebuster19901.license.flags.HeaderFlag;
import com.gamebuster19901.license.flags.HeaderFlags;

public final class CheckerSettings {
	
	private HashMap<String, String> extensions = new HashMap<String, String>();
	private HashMap<String, HeaderFlags> flags = new HashMap<String, HeaderFlags>();
	private HashSet<String> excludePaths = new HashSet<String>();
	
	transient final File CURRENT_DIRECTORY; 
	transient String currentExtension = null;
	transient String currentMessage = null;
	transient HeaderFlags currentMode = null;
	
	{
		try {
			String path = Paths.get(CheckerSettings.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
			if(path.endsWith(".jar")) {
				File dir = new File(path);
				path = dir.getParent();
			}
			CURRENT_DIRECTORY = new File(path);
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		}
	}
	
	public CheckerSettings() {}
	
	public void addExtension(String extension) {
		if(currentExtension == null) {
			if(extension.startsWith(".")) {
				if(extensions.containsKey(extension)) {
					throw new IllegalArgumentException(extension + " already exists!");
				}
				currentExtension = extension;
				currentMode = new HeaderFlags();
				return;
			}
			throw new IllegalArgumentException("Extensions must begin with '.'");
		}
		else {
			throw new IllegalStateException("Finish editing your current extension before modifying a new one!");
		}
	}
	
	public void removeExtension(String extension) {
		if(extensions.remove(extension) == null) {
			System.out.println(extension + " doesn't exist, can't remove");
		}
		else {
			flags.remove(extension);
			System.out.println("removed " + extension);
		}
	}
	
	public void addMessage(String message) {
		if(currentMessage == null) {
			currentMessage = message + "\n";
		}
		else {
			currentMessage += message + "\n";
		}
	}
	
	public void addExclusion(String path) {
		excludePaths.add(path);
	}
	
	public void removeExclusion(String path) {
		excludePaths.remove(path);
	}
	
	public String[] getExclusions() {
		return excludePaths.toArray(new String[] {});
	}
	
	public boolean isIncluded(File f) {
		for(String excluded : excludePaths) {
			try {
				if(f.getCanonicalPath().contains(new File(excluded).getCanonicalPath())) {
					return false;
				}
			} catch (IOException e) {
				throw new IOError(e);
			}
		}
		return true;
	}
	
	public void finishExtension() {
		if(currentMode == null) {
			System.out.println("Cannot finish, mode is not set!");
		}
		else {
			currentMessage = currentMessage.trim();
			extensions.put(currentExtension, currentMessage);
			flags.put(currentExtension, currentMode);
			clear();
		}
	}
	
	public String getExtension() {
		return currentExtension;
	}
	
	public String[] getExtensions() {
		return extensions.keySet().toArray(new String[]{});
	}
	
	public boolean hasExtension(String extension) {
		return extensions.containsKey(extension);
	}
	
	public String getMessage() {
		return currentMessage;
	}
	
	public String getMessage(String extension) {
		return extensions.get(extension);
	}
	
	public HeaderFlags getMode(String extension) {
		return flags.get(extension);
	}
	
	public String[] getMessages() {
		String[] messages = new String[extensions.size()];
		int i = 0;
		for(Entry<String, String> s : extensions.entrySet()) {
			messages[i] = s.getValue();
			i++;
		}
		return messages;
	}
	
	public void clear() {
		currentExtension = null;
		currentMessage = null;
		currentMode = null;
	}
	
	public void validate() throws Exception{
		if(extensions.isEmpty()) {
			throw new IllegalStateException("no extensions specified in checker");
		}
		if(flags.isEmpty()) {
			throw new IllegalStateException("no modes specified in checker");
		}
		if(extensions.size() != flags.size()) {
			throw new IllegalStateException("extensions.size() != modes.size()");
		}
		if(currentExtension != null || currentMessage != null || currentMode != null) {
			throw new IllegalStateException("Transient fields were somehow set!");
		}
		
		for(String s : extensions.keySet()) {
			if(!s.startsWith(".")) {
				throw new IllegalStateException("Extension '" + s + "' does not start with '.'");
			}
			if(flags.get(s) == null) {
				throw new NullPointerException("Mode for '" + s + "' is null");
			}
		}
		
		for(Entry<String, HeaderFlags> entry: flags.entrySet()) {
			HeaderFlags mode = entry.getValue();
			mode.validate();
			for(HeaderFlag flag : mode) {
				flag.validate(this, entry.getKey(), extensions.get(entry.getKey()));
			}
			if(mode.is(FileFlag.class)) {
				File f = new File(extensions.get(entry.getKey()));
				if(!f.exists()) {
					throw new FileNotFoundException("Could not find license file for extension " + entry.getKey() + "\n\n" + f.getAbsolutePath());
				}
				if(f.isDirectory()) {
					throw new NotFileException(f.getAbsolutePath());
				}
			}
		}
	}
}
