package com.gamebuster19901.license.create;

public class List extends Command{

	public List() {
		super("/list", "[<boolean:asJson>]", "prints the list of extensions", "prints the list of extensions");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		if(params.isEmpty() || params.equals("false")) {
			String[] extensions = CreateChecker.getSettings().getExtensions();
			String[] messages = CreateChecker.getSettings().getMessages();
			System.out.println("==========================");
			for(int i = 0; i < extensions.length; i++) {
				System.out.println(extensions[i]);
				System.out.println(messages[i]);
				System.out.println();
			}
			System.out.println("==========================");
		}
		else if(params.equals("true")) {
			System.out.println(CreateChecker.GSON.toJson(CreateChecker.checkerSettings));
		}
		else {
			new IllegalArgumentException(params).printStackTrace();
		}
	}
	
}
