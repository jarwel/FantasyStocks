package com.fantasyfinance.model;

import java.util.List;

import com.google.common.collect.Lists;

public class Player {
	// Will be a parse user

	private final String name;
	private final double funds;

	public Player(String name, double funds) {
		super();
		this.name = name;
		this.funds = funds;
	}

	public String getName() {
		return name;
	}

	public double getFunds() {
		return funds;
	}

	@Override
	public String toString() {
		return String.format("%s - $%.2f", name, funds);
	}

	@Deprecated
	public static List<Player> getMockPlayers() {
		List<Player> players = Lists.newArrayList();
		players.add(new Player("Jason", 10200.99));
		players.add(new Player("Mark", 8765.23));
		return players;
	}

}
