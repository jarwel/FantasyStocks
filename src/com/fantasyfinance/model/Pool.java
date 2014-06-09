package com.fantasyfinance.model;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

public class Pool {

	private final String name;
	private final List<Player> players;

	public Pool(String name, List<Player> players) {
		super();
		this.name = name;
		this.players = players;
	}

	public String getName() {
		return name;
	}

	public List<Player> getPlayer() {
		return players;
	}

	@Override
	public String toString() {
		return name;
	}

	@Deprecated
	public static List<Pool> getMockPools() {
		List<Pool> pools = Lists.newArrayList();
		pools.add(new Pool("Pool 1", Lists.<Player> newArrayList()));
		pools.add(new Pool("Pool 2", Lists.<Player> newArrayList()));
		return pools;
	}
	
	@Deprecated
	public static List<Pool> getMockPoolsWithPlace() {
		List<Pool> pools = Lists.newArrayList();
		pools.add(new Pool(String.format("Pool 1 - %sth place", new Random().nextInt(5) + 4), Lists.<Player> newArrayList()));
		pools.add(new Pool(String.format("Pool2 - %sth place", new Random().nextInt(5) + 4), Lists.<Player> newArrayList()));
		return pools;
	}
}
