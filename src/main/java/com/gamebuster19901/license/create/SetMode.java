package com.gamebuster19901.license.create;

public class SetMode extends Command{

	public SetMode() {
		super("/setmode", "<append|replace>", "sets the header mode for the current header", 
				"sets the header mode for the current header\n\n"
				+ "can be either 'append' or 'replace'\n\n"
				+ "append simply appends the header to the top of the file, while replace will\n"
				+ "overwrite the top lines of the file with the license header\n");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		if(CreateChecker.checkerSettings.getExtension() != null) {
			for(HeaderMode mode : HeaderMode.values()) {
				if(params.equalsIgnoreCase(mode.name())) {
					CreateChecker.checkerSettings.currentMode = mode;
					return;
				}
			}
			System.out.println(params + " is not a valid mode!");
		}
		else {
			System.out.println("You must be editing a header to set it's mode!");
		}
	}

}
