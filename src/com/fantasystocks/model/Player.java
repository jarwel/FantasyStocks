package com.fantasystocks.model;

import java.util.List;

import com.google.common.collect.Lists;
import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("Player")
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
	
	@Override
	public String toString() {
		return String.format("%s - $%.2f", getPlayer().getUsername(), getFunds());
	}

	@Deprecated
	public static List<Player> getMockPlayers() {
		List<Player> players = Lists.newArrayList();
		players.add(new Player(ParseUser.getCurrentUser(), 10200.99));
		players.add(new Player(ParseUser.getCurrentUser(), 8000.99));
		return players;
	} 
}
