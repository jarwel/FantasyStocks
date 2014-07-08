package com.fantasystocks;

import java.text.DecimalFormat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.fantasystocks.client.YahooFinanceClient;
import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Pool;
import com.fantasystocks.model.Portfolio;
import com.fantasystocks.utils.Utils;
import com.parse.Parse;
import com.parse.ParseObject;

public class RestApplication extends Application {
	public static final DecimalFormat dollarFormat = new DecimalFormat("###,###,###,###,##0.00");

	private static final String PARSE_APPLICATION_ID = "bcmueWmVJzWaQacYZa3bXKHECJMyxzkwUl1plVjz";
	private static final String PARSE_CLIENT_KEY = "0CGF2GuilFib12Jm3rzA3XNwnLtUZ1k8AUzKQNl8";
	private static final String SAUMITRA_APP_ID = "3LtVmg0Ldo375b0ZBb7PCkpMGbMOPbuwNf2ayA80";
	private static final String SAUMITRA_SECRET = "F8NA8wGZJtbJueYnMKwHkJ9RxgGx9v6Sg2VgOb5v";

	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		RestApplication.context = this;
		ParseObject.registerSubclass(Pool.class);
		ParseObject.registerSubclass(Portfolio.class);
		ParseObject.registerSubclass(Lot.class);
		// Parse.initialize(this, SAUMITRA_APP_ID, SAUMITRA_SECRET);
		Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
		Intent i = null;
		if (Utils.isUserLoggedIn()) {
			i = new Intent(this, HomeActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		} else {
			i = new Intent(this, LoginActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}
		startActivity(i);
	}

	public static YahooFinanceClient getFinanceClient() {
		return YahooFinanceClient.getInstance(RestApplication.context);
	}
}
