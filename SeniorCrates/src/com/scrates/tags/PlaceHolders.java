package com.scrates.tags;

import java.util.HashMap;
import java.util.Map;

public class PlaceHolders {

	private Map<String, String> placeholder;
	
	public PlaceHolders() {
		this.placeholder = new HashMap<String, String>();
	}
	
	public void addPlaceholder(String key, String placeholder) {
		this.placeholder.put(key, placeholder);
	}
	
	public Map<String, String> getPlaceholders() {
		return this.placeholder;
	}
	
}
