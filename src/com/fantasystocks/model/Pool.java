package com.fantasystocks.model;

import java.io.Serializable;
import java.util.Random;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Pool")
public class Pool extends ParseObject implements Serializable {

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}

	public int getPlayers() {
		return getInt("players");
	}

	public void setPlayers(int players) {
		put("players", players);
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

	public String getRank(Player player) {
		return String.format("%sth", new Random().nextInt(7) + 3);
	}

	public String getGain(Player player) {
		return String.format("+$%.2f", new Random().nextDouble());
	}

	@Override
	public String toString() {
		return getName();
	}

}
