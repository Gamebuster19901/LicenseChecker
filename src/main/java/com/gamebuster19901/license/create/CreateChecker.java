package com.gamebuster19901.license.create;

import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CreateChecker {

	static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	static CheckerSettings checkerSettings = new CheckerSettings();
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("For a list of commands, type /help");
		Scanner scanner = new Scanner(System.in);
		scanner:
		while(scanner.hasNextLine()) {
			String input = scanner.nextLine();
			for(Command c : Command.getCommands()) {
				if(c.matches(input)) {
					int index = input.length();
					if(input.indexOf(' ') != -1) {
						index = input.indexOf(' ');
					}
					String params = input.substring(index);
					c.exec(params.trim());
					if(checkerSettings.getExtension() != null) {
						System.out.println("Enter your license header, including any escape characters.");
					}
					continue scanner;
				}
			}
			if(checkerSettings.getExtension() != null) {
				checkerSettings.addMessage(input);
				System.out.println("Current header:");
				System.out.println(checkerSettings.getMessage());
			}
			else {
				int index = input.length();
				if(input.indexOf(' ') != -1) {
					index = input.indexOf(' ');
				}
				System.out.println("Unrecognized command: " + input.substring(0, index));
			}
		}
	}
	
	public static CheckerSettings getSettings() {
		return checkerSettings;
	}
	
}
