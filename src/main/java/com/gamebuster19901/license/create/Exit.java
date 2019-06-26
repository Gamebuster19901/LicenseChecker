package com.gamebuster19901.license.create;

final class Exit extends Command{

	Exit() {
		super("/exit", "closes this program");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		System.exit(0);
	}

}
