package com.gamebuster19901.license.create;

public class Exclude extends Command{

	public Exclude() {
		super("/exclude", "<path|file>", "marks a directory or file for exclusion");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		CreateChecker.checkerSettings.addExclusion(params);
	}

}
