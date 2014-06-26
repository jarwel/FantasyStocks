package com.fantasystocks.utils;

import java.util.Random;

import com.fantasystocks.R;

public class Utils {
	private static final int TOTAL_IMAGES = 8;
	private static final String POOL_IMAGES_PREFIX = "pool_img_";
	public static String getRandomImageUrl() {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(TOTAL_IMAGES);
		randomInt = randomInt >= 1 && randomInt <= 8 ? randomInt : 1;
		return POOL_IMAGES_PREFIX + randomInt;
	}
	public static int getDrawableResouceForAGivenImageUrl(String imageUrl) {
		int id = R.drawable.pool_img_1;
		int imageNumber = 1;
		if (imageUrl != null) {
			try {
				imageNumber = Integer.parseInt(imageUrl.substring(imageUrl.length() - 1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		switch (imageNumber) {
		case 8:
			id = R.drawable.pool_img_8;
			break;
		case 2:
			id = R.drawable.pool_img_2;
			break;
		case 3:
			id = R.drawable.pool_img_3;
			break;
		case 4:
			id = R.drawable.pool_img_4;
			break;
		case 5:
			id = R.drawable.pool_img_5;
			break;
		case 6:
			id = R.drawable.pool_img_6;
			break;
		case 7:
			id = R.drawable.pool_img_7;
			break;
		default:
			id = R.drawable.pool_img_1;
			break;

		}
		return id;
	}
}
