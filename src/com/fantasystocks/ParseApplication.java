package com.fantasystocks;

import android.app.Application;

import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Player;
import com.fantasystocks.model.Pool;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
	private static final String PARSE_APPLICATION_ID = "bcmueWmVJzWaQacYZa3bXKHECJMyxzkwUl1plVjz";
	private static final String PARSE_CLIENT_KEY = "0CGF2GuilFib12Jm3rzA3XNwnLtUZ1k8AUzKQNl8";
	private static final String SAUMITRA_APP_ID = "3LtVmg0Ldo375b0ZBb7PCkpMGbMOPbuwNf2ayA80";
	private static final String SAUMITRA_SECRET = "F8NA8wGZJtbJueYnMKwHkJ9RxgGx9v6Sg2VgOb5v";

	@Override
	public void onCreate() {
		super.onCreate();
		ParseObject.registerSubclass(Pool.class);
		ParseObject.registerSubclass(Lot.class);
		// ParseObject.registerSubclass(Player.class);
		// Parse.initialize(this, SAUMITRA_APP_ID, SAUMITRA_SECRET);
		Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
	}
}
