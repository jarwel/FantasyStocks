package com.fantasyfinance;

import android.app.Application;

import com.fantasyfinance.model.Player;
import com.parse.Parse;
import com.parse.ParseObject;

public class FantasyFinanceApplication extends Application {
	private static final String SAUMITRA_APP_ID = "3LtVmg0Ldo375b0ZBb7PCkpMGbMOPbuwNf2ayA80";
	private static final String SAUMITRA_SECRET = "F8NA8wGZJtbJueYnMKwHkJ9RxgGx9v6Sg2VgOb5v";
	@Override
	public void onCreate() {
		super.onCreate(); 
		ParseObject.registerSubclass(Player.class);
		Parse.initialize(this, SAUMITRA_APP_ID, SAUMITRA_SECRET);
	
	}
}
