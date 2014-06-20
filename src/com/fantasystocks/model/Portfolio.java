package com.fantasystocks.model;

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

		return new Portfolio(100.00, Lists.<Lot> newArrayList());
	}

}
