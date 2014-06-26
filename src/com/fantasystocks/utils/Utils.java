package com.fantasystocks.utils;

import java.util.Random;

import com.parse.ParseUser;

public class Utils {
	private static final int TOTAL_IMAGES = 8;
	private static final String POOL_IMAGES_PREFIX = "pool_img_";
	
	public static boolean isUserLoggedIn() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			return false;
		} 
		return true;
	}
	
	public static String getRandomImageUrl() {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(TOTAL_IMAGES);
		randomInt = randomInt >= 1 && randomInt <= 8 ? randomInt : 1;
		return POOL_IMAGES_PREFIX + randomInt;
	}
}
