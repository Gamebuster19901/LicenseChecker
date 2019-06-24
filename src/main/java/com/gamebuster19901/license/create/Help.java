package com.gamebuster19901.license.create;

public class Help extends Command{

	public Help() {
		super("/help", "[<string:command>]", "prints this help prompt, or details on how to use a command");
	}

	@Override
	public void exec(String params) {
		if(params.isEmpty()) {
			System.out.println("===============/help===============");
			for(Command c : Command.getCommands()) {
				System.out.println(c);
			}
			System.out.println("===================================");
			return;
		}
		else {
			for(Command c : Command.getCommands()) {
				if (params.equals(c.command.substring(1, c.command.length()))) {
					System.out.println(c.longDescription);
					return;
				}
			}
			System.out.println("Could not find command: " + params);
		}
	}
}
