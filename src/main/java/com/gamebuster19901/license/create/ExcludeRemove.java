package com.gamebuster19901.license.create;

public class ExcludeRemove extends Command{

	public ExcludeRemove() {
		super("/excluderemove", "<path|file>", "Removes an exclusion");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		CreateChecker.checkerSettings.removeExclusion(params);
	}

}
