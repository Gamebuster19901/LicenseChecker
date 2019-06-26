package com.gamebuster19901.license;

import java.nio.file.FileSystemException;

/**
 * Checked exception thrown when a file system operation, intended for a file, fails because the file is a directory.
 */
public class NotFileException extends FileSystemException{

	public NotFileException(String file) {
		super(file);
	}

}
