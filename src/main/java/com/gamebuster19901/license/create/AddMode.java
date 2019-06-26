package com.gamebuster19901.license.create;

public class AddMode extends Command{

	public AddMode() {
		super("/addmode", "<append|insert|file|string>", "adds a header mode for the current header", 
				"adds the header mode for the current header\n\n"
				+ "can be either 'append, 'insert', 'file' or 'string'\n\n"
						
				+ "append appends the header to the top of the file\n"
				+ "insert will insert the header 1 line below the top of the file (used for json)\n"
				+ "if neither append nor insert are specified, it will default to append"
				
				+ "file will read the license text from a file instead of the raw string"
				+ "string will read the license text from the raw string"
				+ "if neither file nor string is specified, it will default to string"
				+ ""
				+ "append and insert are mutually exclusive"
				+ "file and string are mutually exclusive");
	}

	@Override
	public void exec(String params) throws InterruptedException {
		if(CreateChecker.checkerSettings.getExtension() != null) {
			for(HeaderMode mode : HeaderMode.values()) {
				if(params.equalsIgnoreCase(mode.name())) {
					CreateChecker.checkerSettings.currentMode.add(mode);
					return;
				}
			}
			System.out.println(params + " is not a valid mode!");
		}
		else {
			System.out.println("You must be editing a header to set its mode!");
		}
	}

}
