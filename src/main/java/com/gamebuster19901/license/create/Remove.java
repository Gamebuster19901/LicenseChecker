package com.gamebuster19901.license.create;

public class Remove extends Command{

	public Remove() {
		super("/remove", "<string:extension>", "Removes an extension and its license from the list");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		if(params.isEmpty()) {
			throw new IllegalArgumentException();
		}
		CreateChecker.checkerSettings.removeExtension(params);
	}

}
