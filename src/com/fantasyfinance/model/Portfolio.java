package com.fantasyfinance.model;

import java.util.List;

import com.google.common.collect.Lists;

public class Portfolio {

	private final double cash;
	private final List<Lot> lots;

	public Portfolio(double cash, List<Lot> lots) {
		super();
		this.cash = cash;
		this.lots = lots;
	}

	public double getCash() {
		return cash;
	}

	public List<Lot> getLots() {
		return lots;
	}

	@Deprecated
	public static Portfolio getMockPorfolio() {
		List<Lot> lots = Lists.newArrayList();
		lots.add(new Lot("YHOO", 100, 1000.00));
		lots.add(new Lot("FB", 100, 1000.00));
		lots.add(new Lot("GOOGL", 100, 1000.00));
		return new Portfolio(100.00, lots);
	}

}
