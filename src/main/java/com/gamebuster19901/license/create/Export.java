package com.gamebuster19901.license.create;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

public class Export extends Command{

	public static final File EXPORT_LOCATION;
	static{
		try {
			EXPORT_LOCATION = new File(new File(Export.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString() + "/licenses.json");
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		}
	}
	
	public Export() {
		super("/export", "[<string:fullPathToFile>]", "Exports all license headers to a json file");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		try {
			File exportLoc = EXPORT_LOCATION;
			if(!params.isEmpty()) {
				exportLoc = new File(params);
			}
			String json = CreateChecker.GSON.toJson(CreateChecker.getSettings());
			exportLoc.delete();
			exportLoc.getParentFile().mkdirs();
			exportLoc.createNewFile();
			Files.asCharSink(exportLoc, Charset.defaultCharset(), FileWriteMode.APPEND).write(json);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
