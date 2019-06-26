/*
 * Copyright 2019 Gamebuster19901
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.gamebuster19901.license.create;

import static com.gamebuster19901.license.create.HeaderMode.*;

import java.util.ArrayList;
import java.util.Iterator;

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
