package com.gamebuster19901.license.create;

public class Undo extends Command{

	public Undo() {
		super("/undo", "removes the last line from the current license you're editing");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		CheckerSettings settings = CreateChecker.checkerSettings;
		if(settings.getExtension() != null) {
			int index = settings.currentMessage.lastIndexOf("\n");
			if(index == -1) {
				settings.currentMessage = "";
			}
			else {
				settings.currentMessage = settings.currentMessage.substring(0, index);
				index = settings.currentMessage.lastIndexOf("\n");
				if(index == -1) {
					settings.currentMessage = "";
					return;
				}
				settings.currentMessage = settings.currentMessage.substring(0, index);
				settings.addMessage("");
			}
			System.out.println("Current header:");
			System.out.println(settings.getMessage());
		}
		else {
			System.out.println("Not editing, noting to undo!");
		}
	}

}
