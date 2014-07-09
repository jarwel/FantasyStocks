package com.fantasystocks.model;

import java.io.Serializable;
import java.util.Locale;
import java.util.Random;

import com.fantasystocks.util.Utils;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

@ParseClassName("Pool")
public class Pool extends ParseObject implements Serializable {
	private static final long serialVersionUID = -8673978233324396984L;

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}

	public int getPlayerLimit() {
		return getInt("playerLimit");
	}

	public void setPlayerLimit(int playerLimit) {
		put("playerLimit", playerLimit);
	}

	public double getFunds() {
		return getDouble("funds");
	}

	public void setFunds(double funds) {
		put("funds", funds);
	}

	public void setCanonicalName(String name) {
		name = name.toLowerCase(Locale.getDefault());
		put("canonicalName", name);
	}

	public String getCanonicalName() {
		return getString("canonicalName");
	}

	public String getPoolImageUrl() {
		return getString("poolImageUrl");
	}

	public void setPoolImageUrl() {
		String imageUrl = Utils.getRandomImageUrl();
		put("poolImageUrl", imageUrl);
	}

	public String getRank(Portfolio portfolio) {
		return String.format("%sth", new Random().nextInt(7) + 3);
	}

	public String getChange(Portfolio portfolio) {
		String sign = new Random().nextInt(8) > 4 ? "+" : "-";
		return String.format(sign + "$%.2f", new Random().nextDouble());
	}

	public void addPortfolio(final ParseUser user, final SaveCallback callback) {
		fetchIfNeededInBackground(new GetCallback<Pool>() {
			@Override
			public void done(Pool pool, ParseException parseException) {
				if (parseException != null) {
					callback.done(parseException);
				}
				Portfolio portfolio = new Portfolio();
				portfolio.setUser(user);
				portfolio.setPool(pool);
				portfolio.setCash(pool.getFunds());
				portfolio.saveInBackground(callback);
			}
		});
	}

}
