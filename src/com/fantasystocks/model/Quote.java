package com.fantasystocks.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;

public class Quote {

	private String symbol;
	private String name;
	private double price;

	public static Quote fromJSONObject(JSONObject json) {
		try {
			Quote quote = new Quote();
			JSONObject object = json.getJSONObject("query").getJSONObject("results").getJSONObject("quote");
			quote.symbol = object.getString("symbol");
			quote.name = object.getString("Name");
			quote.price = object.getDouble("LastTradePriceOnly");
			return quote;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Quote> fromJSONArray(JSONObject json) {
		List<Quote> quotes = Lists.newArrayList();
		try {
			JSONArray array = json.getJSONObject("query").getJSONObject("results").getJSONArray("quotes");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				if (object != null) {
					quotes.add(Quote.fromJSONObject(object));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return quotes;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

}
