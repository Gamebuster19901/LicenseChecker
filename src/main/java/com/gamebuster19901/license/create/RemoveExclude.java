package com.gamebuster19901.license.create;

public class RemoveExclude extends Command{

	public RemoveExclude() {
		super("/removeexclude", "<path|file>", "Removes an exclusion");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		CreateChecker.checkerSettings.removeExclusion(params);
	}

}
