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

import static com.gamebuster19901.license.flags.HeaderFlagType.*;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import com.gamebuster19901.license.JsonFlag;
import com.gamebuster19901.license.create.CheckerSettings;
import com.google.common.io.Files;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public abstract class HeaderFlag implements Comparable{
	
	private static final ArrayList<HeaderFlag> FLAGS = new ArrayList<HeaderFlag>();
	public static final JsonDeserializer<HeaderFlag> DESERIALIZER;
	
	private transient final ArrayList<String> exclusivities = new ArrayList<String>();
	private transient final HeaderFlagType flagType;
	private transient final String detailMessage;
	private transient int priority;
	
	private final String name;
	
	static {
		new AppendFlag();
		new JsonFlag();
		new FileFlag();
		new StringFlag();
	}
	
	public static abstract class LocationFlag extends HeaderFlag {
		
		public LocationFlag(String name, String detailMessage, int priority) {
			super(LOCATION, name, detailMessage, priority);
		}
		
		public abstract byte[] getHeader(CheckerSettings settings, String location) throws Exception;
		
	}
	
	public static abstract class DataTypeFlag extends HeaderFlag {
		
		public DataTypeFlag(String name, String detailMessage, int priority) {
			super(DATATYPE, name, detailMessage, priority);
		}
		
		public abstract void apply(File f) throws Exception;
		
	}
	
	public HeaderFlag (HeaderFlagType type, String name, String detailMessage, int priority) {
		this.flagType = type;
		this.name = name;
		this.priority = priority;
		this.detailMessage = this.name() + ": " + detailMessage + "\n";
		while(FLAGS.contains(this)) {
			FLAGS.remove(this);
		}
		FLAGS.add(this);
		Collections.sort(FLAGS);
	}
	
	public final HeaderFlag exclude(String... names) {
		for(String name : names) {
			while(exclusivities.contains(name)) {
				exclusivities.remove(name);
			}
			HeaderFlag flag = getFlag(name);
			if(flag != null) {
				flag.exclusivities.add(name);
			}
			exclusivities.add(name);
			Collections.sort(exclusivities);
		}
		return this;
	}
	
	public final boolean isMututallyExclusive(HeaderFlag mode) {
		return exclusivities.contains(mode.name);
	}
	
	public final String[] getExclusivities() {
		return exclusivities.toArray(new String[]{});
	}

	public final String name() {
		return this.toString();
	}
	
	public final String toString() {
		return name;
	}
	
	public final void setPriority(int priority) {
		this.priority = priority;
		Collections.sort(FLAGS);
	}
	
	public final int getPriority() {
		return priority;
	}
	
	public final String getDetailMessage() {
		return detailMessage;
	}

	@Override
	public final int compareTo(Object o) {
		HeaderFlag flag = (HeaderFlag) o;
		if(priority < flag.getPriority()) {
			return -1;
		}
		if(priority > flag.getPriority()) {
			return 1;
		}
		return name().compareTo(flag.name());
	}
	
	
	public abstract void validate(CheckerSettings settings, String extension, String value) throws Exception;
	
	public static HeaderFlag[] getEditFlags() {
		return FLAGS.toArray(new HeaderFlag[]{});
	}
	
	public static final HeaderFlag getFlag(String name) {
		for(HeaderFlag f : FLAGS) {
			if(f.name().equalsIgnoreCase(name)) {
				return f;
			}
		}
		return null;
	}
	
	static {
		DESERIALIZER = new JsonDeserializer<HeaderFlag>() {

			@Override
			public HeaderFlag deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				JsonObject json = jsonElement.getAsJsonObject();
				
				String name = json.get("name").getAsString();
				
				HeaderFlag flag = HeaderFlag.getFlag(name);
				if(name != null) {
					return flag;
				}
				
				throw new JsonParseException("Editflag \'" + name + "' does not exist! Do you have your dependencies installed?");
			}
			
		};
	}
	
	public static String getExtension(File file) {
		return "." + Files.getFileExtension(file.getAbsolutePath());
	}
	
}
