package com.askaround.mobile.android.utils;

import java.util.Locale;

public class Config {

	
	
	public static final String ACCESS_KEY_ID = "AKIAJQ6W74JGMIBUEIOQ";
	public static final String SECRET_KEY = "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";
	
	public static final String PICTURE_BUCKET = "askaroundbucket";
	public static final String PICTURE_NAME = "NameOfThePicture";
	
	
	public static String getPictureBucket() {
		return ("askaround" + ACCESS_KEY_ID + PICTURE_BUCKET).toLowerCase(Locale.US);
	}
	
	
}
