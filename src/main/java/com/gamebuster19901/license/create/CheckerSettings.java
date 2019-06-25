package com.gamebuster19901.license.create;

import java.util.HashMap;
import java.util.Map.Entry;

public final class CheckerSettings {
	
	private HashMap<String, String> extensions = new HashMap<String, String>();
	private HashMap<String, HeaderMode> modes = new HashMap<String, HeaderMode>();
	
	transient String currentExtension = null;
	transient String currentMessage = null;
	transient HeaderMode currentMode = null;
	
	public CheckerSettings() {}
	
	public void addExtension(String extension) {
		if(currentExtension == null) {
			if(extension.startsWith(".")) {
				if(extensions.containsKey(extension)) {
					throw new IllegalArgumentException(extension + " already exists!");
				}
				currentExtension = extension;
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
	
	public void finishExtension() {
		if(currentMode == null) {
			System.out.println("Cannot finish, mode is not set!");
		}
		else {
			extensions.put(currentExtension, currentMessage);
			modes.put(currentExtension, currentMode);
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
	
	public HeaderMode getMode(String extension) {
		return modes.get(extension);
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
		if(modes.isEmpty()) {
			throw new IllegalStateException("no modes specified in checker");
		}
		if(extensions.size() != modes.size()) {
			throw new IllegalStateException("extensions.size() != modes.size()");
		}
		if(currentExtension != null || currentMessage != null || currentMode != null) {
			throw new IllegalStateException("Transient fields were somehow set!");
		}
		
		for(String s : extensions.keySet()) {
			if(!s.startsWith(".")) {
				throw new IllegalStateException("Extension '" + s + "' does not start with '.'");
			}
			if(modes.get(s) == null) {
				throw new NullPointerException("Mode for '" + s + "' is null");
			}
		}
	}
	
}
