package com.fantasystocks.model;

import com.parse.ParseUser;

public class Player extends ParseUser {

	// Ensure that your subclass has a public default constructor
	public Player() {
		super();
	}

	// Add a constructor that contains core properties
	public Player(ParseUser player, double funds) {
		super();
		setFunds(funds);
		setPlayer(player);
	}

	public double getFunds() {
		return getDouble("funds");
	}

	// Use put to modify field values
	public void setFunds(double value) {
		put("funds", value);
	}

	public ParseUser getPlayer() {
		return getParseUser("player");
	}

	// Associate each item with a user
	public void setPlayer(ParseUser player) {
		put("player", player);
	}

}
