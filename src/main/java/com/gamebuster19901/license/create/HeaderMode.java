package com.gamebuster19901.license.create;

public enum HeaderMode {
	APPEND,
	INSERT,
	FILE,
	STRING;
	
	public boolean mututallyExclusive(HeaderMode mode) {
		return
			this == APPEND && mode == INSERT ||
			this == INSERT && mode == APPEND ||
			this == FILE && mode == STRING ||
			this == STRING && mode == FILE;
	}
}
