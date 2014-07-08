package com.fantasystocks.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Lot")
public class Lot extends ParseObject {

	public Portfolio getPortfolio() {
		return (Portfolio) getParseObject("portfolio");
	}

	public void setPortfolio(Portfolio portfolio) {
		put("portfolio", portfolio);
	}

	public String getSymbol() {
		return getString("symbol");
	}

	public void setSymbol(String symbol) {
		put("symbol", symbol);
	}

	public int getShares() {
		return getInt("shares");
	}

	public void setShares(int shares) {
		put("shares", shares);
	}

	public double getCostBasis() {
		return getDouble("costBasis");
	}

	public void setCostBasis(double costBasis) {
		put("costBasis", costBasis);
	}

}
