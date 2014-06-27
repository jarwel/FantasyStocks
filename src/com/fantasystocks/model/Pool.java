package com.fantasystocks.model;

import java.io.Serializable;
import java.util.Random;

import android.util.Log;

import com.fantasystocks.utils.Utils;
import com.parse.ParseClassName;
import com.parse.ParseObject;

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

	public String getPoolImageUrl() {
		return getString("poolImageUrl");
	}

	public void setPoolImageUrl() {
		String imageUrl = Utils.getRandomImageUrl();
		put("poolImageUrl", imageUrl);
	}

	public String getRank(Player portfolio) {
		return String.format("%sth", new Random().nextInt(7) + 3);
	}

	public String getGain(Player portfolio) {
		String sign = new Random().nextInt(8) > 4 ? "+" : "-";
		return String.format(sign + "$%.2f", new Random().nextDouble());
	}

}
