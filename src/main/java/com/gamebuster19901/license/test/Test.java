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

package com.gamebuster19901.license.test;

import java.util.Arrays;

public class Test {

	public static void main(String[] args) {
		System.out.println(Arrays.toString(args));
		boolean errored = false;
		try {
			assert false;
		}
		catch(AssertionError e) {
			errored = true;
		}
		if(!errored) {
			throw new AssertionError();
		}
	}
	
}
