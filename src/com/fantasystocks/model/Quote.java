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
			JSONObject quoteObject = json.getJSONObject("query").getJSONObject("results").getJSONObject("quote");
			return parseQuote(quoteObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Quote> fromJSONArray(JSONObject json) {
		List<Quote> quotes = Lists.newArrayList();
		try {
			JSONObject resultObject = json.getJSONObject("query").getJSONObject("results");
			JSONObject quoteObject = resultObject.optJSONObject("quote");
			if (quoteObject != null) {
				Quote quote = parseQuote(quoteObject);
				if (quote != null) {
					quotes.add(quote);
				}
			} else {
				JSONArray quoteArray = resultObject.optJSONArray("quote");
				for (int i = 0; i < quoteArray.length(); i++) {
					JSONObject object = quoteArray.getJSONObject(i);
					Quote quote = parseQuote(object);
					if (quote != null) {
						quotes.add(quote);
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
