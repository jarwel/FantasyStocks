package com.fantasystocks.model;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Maps;

public class Quote {

	private String symbol;
	private String name;
	private double price;

	public String getSymbol() {
		return symbol;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public static Quote fromJSONObject(JSONObject json) {
		try {
			JSONObject object = json.getJSONObject("query").getJSONObject("results").getJSONObject("quote");
			return parseQuote(object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Quote> fromJSONArray(JSONObject json) {
		Map<String, Quote> quotes = Maps.newHashMap();
		try {
			JSONObject result = json.getJSONObject("query").getJSONObject("results");
			JSONObject object = result.optJSONObject("quote");
			if (object != null) {
				Quote quote = parseQuote(object);
				if (quote != null) {
					quotes.put(quote.getSymbol(), quote);
				}
			} else {
				JSONArray array = result.optJSONArray("quote");
				for (int i = 0; i < array.length(); i++) {
					Quote quote = parseQuote(array.getJSONObject(i));
					if (quote != null) {
						quotes.put(quote.getSymbol(), quote);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return quotes;
	}

	private static Quote parseQuote(JSONObject object) {
		try {
			if ("null".equals(object.getString("ErrorIndicationreturnedforsymbolchangedinvalid"))) {
				Quote quote = new Quote();
				quote.symbol = object.getString("symbol");
				quote.name = object.getString("Name");
				quote.price = object.getDouble("LastTradePriceOnly");
				return quote;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
