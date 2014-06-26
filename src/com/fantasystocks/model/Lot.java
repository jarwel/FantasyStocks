package com.fantasystocks.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Lot")
public class Lot extends ParseObject {

	public Player getPlayer() {
		return (Player) getParseObject("player");
	}

	public void setPlayer(Player player) {
		put("player", player);
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

	@Override
	public String toString() {
		return getSymbol();
	}

}
