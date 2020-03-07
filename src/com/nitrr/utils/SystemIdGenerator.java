package com.nitrr.utils;

public class SystemIdGenerator {

	private static int id_count = 556432;
	
	public static int getNextId() {
		return ++id_count;
	}
	
}
