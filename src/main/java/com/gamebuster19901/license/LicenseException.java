package com.gamebuster19901.license;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings("serial")
public class LicenseException extends Exception {

	public static final String DEFAULT_MESSAGE = "The following files are not licensed correctly:\n\n$1";
	
	public LicenseException(File[] files) {
		this(files, DEFAULT_MESSAGE);
	}
	
	public LicenseException(File[] files, String message) {
		super(message + Arrays.toString(files));
	}
	
	public LicenseException(Collection<File> files) {
		this(files, DEFAULT_MESSAGE);
	}
	
	public LicenseException(Collection<File> files, String message) {
		this((File[])files.toArray(), message);
	}
	
}
