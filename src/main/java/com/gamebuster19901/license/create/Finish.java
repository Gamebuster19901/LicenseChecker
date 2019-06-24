package com.gamebuster19901.license.create;

public class Finish extends Command{

	public Finish() {
		super("/finish", "finishes adding the current extension");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		CheckerSettings settings = CreateChecker.getSettings();
		String extension = settings.getExtension();
		String header = settings.getMessage();
		if(extension != null) {
			if(header != null) {
				settings.finishExtension();
			}
			else {
				System.out.println("Could not finish, there was no header provided!");
			}
		}
		else {
			System.out.println("No extension was being added!");
		}
	}

}
