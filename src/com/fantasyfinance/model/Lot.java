package com.fantasyfinance.model;

public class Lot {

	private final String symbol;
	private final int shares;
	private final double costBasis;

	public Lot(String symbol, int shares, double costBasis) {
		super();
		this.symbol = symbol;
		this.shares = shares;
		this.costBasis = costBasis;
	}

	public String getSymbol() {
		return symbol;
	}

	public int getShares() {
		return shares;
	}

	public double getCostBasis() {
		return costBasis;
	}

	@Override
	public String toString() {
		return symbol;
	}

}
