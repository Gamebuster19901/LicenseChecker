package com.gamebuster19901.license.create;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.io.Files;

public class Import extends Command{

	public Import(String command, String params, String shortDescription) {
		super(command, params, shortDescription);
		// TODO Auto-generated constructor stub
	}

	public Import() {
		super("/import", "[<string:fullPathToFile>]", "Imports license headers from a json file");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		try {
			File importLoc = Export.EXPORT_LOCATION;
			if(!params.isEmpty()) {
				importLoc = new File(params);
			}
			String json = Files.asCharSource(importLoc, Charset.defaultCharset()).read();
			CreateChecker.checkerSettings = CreateChecker.GSON.fromJson(json, CheckerSettings.class);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
