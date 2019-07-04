package com.gamebuster19901.license.flags;

import java.util.ArrayList;

public class HeaderFlagType{
	
	private static final ArrayList<HeaderFlagType> TYPES = new ArrayList<HeaderFlagType>();
	
	public static final HeaderFlagType LOCATION = new HeaderFlagType("location");
	public static final HeaderFlagType DATATYPE = new HeaderFlagType("dataType");
	
	private transient final String name;
	
	public HeaderFlagType(String name) {
		this.name = name;
	}

	public final String getName() {
		return name;
	}
}
