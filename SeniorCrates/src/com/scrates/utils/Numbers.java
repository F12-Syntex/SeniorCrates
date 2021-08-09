package com.scrates.utils;

public class Numbers {

	
	public static boolean isRealNumber(String i) {
		try {
			Integer.parseInt(i);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isDouble(String i) {
		try {
			Double.parseDouble(i);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
}
