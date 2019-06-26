package com.gamebuster19901.license.create;

import java.util.ArrayList;
import java.util.Iterator;
import static com.gamebuster19901.license.create.HeaderMode.*;

public class HeaderModes {

	private ArrayList<HeaderMode> modes = new ArrayList<HeaderMode>() {};
	
	public HeaderModes() {}
	
	public HeaderModes(HeaderMode... modes) {
		for(HeaderMode mode : modes) {
			add(mode);
		}
	}
	
	public void add(HeaderMode mode) {
		for(Iterator<HeaderMode> iterator = modes.iterator(); iterator.hasNext();) {
			HeaderMode curMode = iterator.next();
			if(curMode.mututallyExclusive(mode)) {
				System.out.println("Removing mutually exclusive mode " + curMode);
				iterator.remove();
			}
			if(curMode == mode) {
				return;
			}
		}
		modes.add(mode);
	}
	
	public void remove(HeaderMode mode) {
		modes.remove(mode);
	}
	
	public void validate() {
		for(int i = 0; i < modes.size(); i++) {
			HeaderMode mode1 = modes.get(i);
			for(int j = i + 1; j < modes.size(); j++) {
				HeaderMode mode2 = modes.get(j);
				if(mode1.mututallyExclusive(mode2)) {
					throw new IllegalStateException("Mutually exclusive modes detected: " + mode1 + " and " + mode2);
				}
				if(mode1 == mode2) {
					throw new IllegalStateException("Duplicate modes detected: " + mode1);
				}
			}
		}
	}
	
	public boolean is(HeaderMode mode) {
		System.out.println(mode);
		switch(mode) {
			case JSON:
			case FILE:
				return modes.contains(mode);
			case STRING:
				return modes.contains(STRING) || (!modes.contains(STRING) && !modes.contains(FILE));
			case APPEND:
				return modes.contains(APPEND) || (!modes.contains(APPEND) && !modes.contains(JSON));
			default:
				throw new AssertionError();
		}
	}
	
}
