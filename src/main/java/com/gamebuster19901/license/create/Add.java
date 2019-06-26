package com.gamebuster19901.license.create;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.io.Files;

public class Add extends Command{

	public Add() {
		super(
				"/add", 
				"<string:extension>, [<string:fullPathToFile>]",
				"Adds a license to the checker",
				"====================================================\n"
				+ "Adds a license to the checker\n\n"
				+ "if a file is provided, it will associate its\n"
				+ "contents as a header to the provided extension\n"
				+ "\n"
				+ "If there is no file provided, you will be prompted\n"
				+ "to enter a header to associate for files with the\n"
				+ "provided extension\n"
				+ "\n"
				+ "All files with the associated extension must contain\n"
				+ "the provided text at the top of the file in order to\n"
				+ "pass license checks\n"
				+ "====================================================\n");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		CheckerSettings settings = CreateChecker.getSettings();
		try {
			if(params.isEmpty()) {
				System.out.println("No extension provided");
			}
			else {
				String param1 = params;
				String param2;
				if(param1.indexOf(' ') != -1) {
					param2 = param1.substring(param1.indexOf(' '));
					param1 = param1.substring(0, params.indexOf(' '));
					System.out.println("param1: " + param1);
					System.out.println("param2: " + param2);
					
					Path path = Paths.get(param2);
					File f = path.toFile();
					String header = Files.asCharSource(f, Charset.defaultCharset()).read();
					settings.addExtension(param1);
					settings.addMessage(header.trim());
					settings.finishExtension();
				}
				else {
					settings.addExtension(params);
				}
			}
		}
		catch(Throwable t) {
			t.printStackTrace();
			Thread.sleep(50);
			System.out.println("extension not added");
		}
	}

}
