/*
 * LicenseChecker Copyright 2019 Gamebuster19901
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

package com.gamebuster19901.license.flags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class HeaderFlags implements Iterable<HeaderFlag>{

	private ArrayList<HeaderFlag> flags = new ArrayList<HeaderFlag>() {};
	
	public HeaderFlags() {}
	
	public HeaderFlags(HeaderFlag... flags) {
		for(HeaderFlag flag : flags) {
			add(flag);
		}
	}
	
	public void add(HeaderFlag flag) {
		for(Iterator<HeaderFlag> iterator = flags.iterator(); iterator.hasNext();) {
			HeaderFlag curMode = iterator.next();
			if(curMode.isMututallyExclusive(flag)) {
				System.out.println("Removing mutually exclusive flag " + curMode);
				iterator.remove();
			}
			if(curMode == flag) {
				return;
			}
		}
		flags.add(flag);
		Collections.sort(flags);
	}
	
	public void remove(HeaderFlag flag) {
		flags.remove(flag);
		Collections.sort(flags);
	}
	
	public void validate() {
		for(int i = 0; i < flags.size(); i++) {
			HeaderFlag flag1 = flags.get(i);
			for(int j = i + 1; j < flags.size(); j++) {
				HeaderFlag flag2 = flags.get(j);
				if(flag1.isMututallyExclusive(flag2)) {
					throw new IllegalStateException("Mutually exclusive flags detected: " + flag1 + " and " + flag2);
				}
				if(flag1 == flag2) {
					throw new IllegalStateException("Duplicate flags detected: " + flag1);
				}
			}
		}
	}
	
	public boolean is(Class<? extends HeaderFlag> type) {
		for(HeaderFlag f : flags) {
			if(type.isAssignableFrom(f.getClass())) {
				return true;
			}
		}
		return false;
	}
	
	public HeaderFlag[] getFlags() {
		return flags.toArray(new HeaderFlag[]{});
	}

	@Override
	public Iterator<HeaderFlag> iterator() {
		return ((ArrayList)flags.clone()).iterator();
	}
	
}
