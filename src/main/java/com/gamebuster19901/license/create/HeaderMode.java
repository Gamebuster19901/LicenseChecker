package com.gamebuster19901.license.create;

public enum HeaderMode {
	APPEND,
	JSON,
	FILE,
	STRING;
	
	public boolean mututallyExclusive(HeaderMode mode) {
		return
			this == APPEND && mode == JSON ||
			this == JSON && mode == APPEND ||
			this == FILE && mode == STRING ||
			this == STRING && mode == FILE;
	}
}
