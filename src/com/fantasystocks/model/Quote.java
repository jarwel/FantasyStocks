package com.fantasystocks.model;

import com.google.gson.annotations.SerializedName;

public class Quote {

	private String symbol;

	@SerializedName("LastTradePriceOnly")
	private double price;

	public String getSymbol() {
		return symbol;
	}

	public double getPrice() {
		return price;
	}

}
