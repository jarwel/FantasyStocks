package com.fantasystocks;

import android.app.Application;

import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Pool;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
	private static final String PARSE_APPLICATION_ID = "bcmueWmVJzWaQacYZa3bXKHECJMyxzkwUl1plVjz";
	private static final String PARSE_CLIENT_KEY = "0CGF2GuilFib12Jm3rzA3XNwnLtUZ1k8AUzKQNl8";

	@Override
	public void onCreate() {
		super.onCreate();
		ParseObject.registerSubclass(Pool.class);
		ParseObject.registerSubclass(Lot.class);
		Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
	}
}
