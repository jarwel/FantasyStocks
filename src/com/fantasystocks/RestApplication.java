package com.fantasystocks;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.fantasystocks.client.YahooFinanceClient;
import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Pool;
import com.fantasystocks.model.Portfolio;
import com.fantasystocks.util.FinanceFormat;
import com.fantasystocks.util.Utils;
import com.parse.Parse;
import com.parse.ParseObject;

public class RestApplication extends Application {

	private static final String PARSE_APPLICATION_ID = "bcmueWmVJzWaQacYZa3bXKHECJMyxzkwUl1plVjz";
	private static final String PARSE_CLIENT_KEY = "0CGF2GuilFib12Jm3rzA3XNwnLtUZ1k8AUzKQNl8";
	private static final String SAUMITRA_APP_ID = "8wyPzjmCiypaj1zon8v51ccJ1i2xoboadKXB6wW7";
	private static final String SAUMITRA_SECRET = "wymnEV6RdAFrMfyRKMVcIQg0NZ686ZuUT3WOuGOF";

	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		RestApplication.context = this;
		ParseObject.registerSubclass(Pool.class);
		ParseObject.registerSubclass(Portfolio.class);
		ParseObject.registerSubclass(Lot.class);
		Parse.initialize(this, SAUMITRA_APP_ID, SAUMITRA_SECRET);
		//Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
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

	public static FinanceFormat getFormatter() {
		return FinanceFormat.getInstance(RestApplication.context);
	}
}
