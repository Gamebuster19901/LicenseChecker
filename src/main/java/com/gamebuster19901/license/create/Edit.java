package com.gamebuster19901.license.create;

public class Edit extends Command{

	public Edit() {
		super("/edit", "<string:extension>", "Begin editing an extension");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		CheckerSettings settings = CreateChecker.checkerSettings;
		if(settings.getExtension() == null && settings.hasExtension(params)) {
			settings.currentExtension = params;
			settings.currentMessage = settings.getMessage(params);
			System.out.println("Current header:");
			System.out.println(settings.getMessage());
		}
	}

}
