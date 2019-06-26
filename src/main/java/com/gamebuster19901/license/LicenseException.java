package com.gamebuster19901.license;

import java.io.File;
import java.util.Collection;

@SuppressWarnings("serial")
public class LicenseException extends Exception {

	public static final String DEFAULT_MESSAGE = "The following files are not licensed correctly:\n\n$1";
	
	public LicenseException(Collection<File> files) {
		this(files, DEFAULT_MESSAGE);
	}
	
	public LicenseException(Collection<File> files, String message) {
		super(message + "\n" + listFiles(files));
	}
	
	public LicenseException(File file, Throwable cause) {
		super(file.getAbsolutePath(), cause);
	}
	
	private static String listFiles(Collection<File> files) {
		String message = "";
		for(File f : files) {
			message += f;
			message += '\n';
		}
		return message;
	}
	
}
