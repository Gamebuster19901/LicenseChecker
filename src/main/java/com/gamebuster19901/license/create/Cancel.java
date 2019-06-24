package com.gamebuster19901.license.create;

public class Cancel extends Command{

	public Cancel() {
		super("/cancel", "Cancels the extension you're adding without saving");
	}

	@Override
	public void exec(String params) {
		CheckerSettings settings = CreateChecker.getSettings();
		String extension = settings.getExtension();
		if(extension != null) {
			settings.clear();
			System.out.println("Cancelled the addition of '" + extension + "'");
		}
		else {
			System.out.println("No extension was being added!");
		}
	}
	
}
