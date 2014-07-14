package com.fantasystocks.model;

import java.io.Serializable;
import java.util.Date;
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

	public void setCanonicalName(String name) {
		name = name.toLowerCase(Locale.getDefault());
		put("canonicalName", name);
	}

	public String getCanonicalName() {
		return getString("canonicalName");
	}

	public void setPlayerLimit(int playerLimit) {
		put("playerLimit", playerLimit);
	}

	public int getPlayerLimit() {
		return getInt("playerLimit");
	}

	public void setStartDate(Date startDate) {
		put("startDate", startDate);
	}

	public Date getStartDate() {
		return getDate("startDate");
	}

	public void setEndDate(Date endDate) {
		put("endDate", endDate);
	}

	public Date getEndDate() {
		return getDate("endDate");
	}

	public void setPlayerCount(int playerCount) {
		put("playerCount", playerCount);
	}

	public int getPlayerCount() {
		return getInt("playerCount");
	}

	public double getFunds() {
		return getDouble("funds");
	}

	public void setFunds(double funds) {
		put("funds", funds);
	}

	public String getPoolImageUrl() {
		return getString("poolImageUrl");
	}

	public void setPoolImageUrl() {
		String imageUrl = Utils.getRandomImageUrl();
		put("poolImageUrl", imageUrl);
	}

	public boolean isOpen() {
		return getPlayerCount() < getPlayerLimit();
	}

	public int getOpenCount() {
		if (isOpen()) {
			return getPlayerLimit() - getPlayerCount();
		}
		return 0;
	}

	public String getRank(Portfolio portfolio) {
		return String.format("%sth", new Random().nextInt(7) + 3);
	}

	public void addPortfolio(final ParseUser user, final SaveCallback callback) {
		fetchIfNeededInBackground(new GetCallback<Pool>() {
			@Override
			public void done(final Pool pool, ParseException parseException) {
				if (parseException != null) {
					callback.done(parseException);
				}
				Portfolio portfolio = new Portfolio();
				portfolio.setUser(user);
				portfolio.setPool(pool);
				portfolio.setCash(pool.getFunds());
				portfolio.setStartingFunds(pool.getFunds());
				portfolio.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException parseException) {
						if (parseException != null) {
							callback.done(parseException);
						}
						pool.add("players", user);
						pool.put("playerCount", pool.getPlayerCount() + 1);
						pool.saveInBackground(callback);
					}
				});
			}
		});
	}

}
