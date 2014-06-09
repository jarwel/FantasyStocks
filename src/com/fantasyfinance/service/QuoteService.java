package com.fantasyfinance.service;

import java.util.Map;
import java.util.Set;

import com.fantasyfinance.client.YahooFinanceClient;
import com.google.common.collect.Maps;

public class QuoteService {
	private static QuoteService instance;

	private final Map<String, Double> quotes = Maps.newHashMap();

	public static QuoteService getInstance() {
		if (instance == null) {
			instance = new QuoteService();
		}
		return instance;
	}

	public Double getQuote(String symbol) {
		return quotes.get(symbol);
	}

	public Map<String, Double> getQuotes(Set<String> symbols) {
		Map<String, Double> quotes = Maps.newHashMap();
		for (String symbol : symbols) {
			Double price = getQuote(symbol);
			if (price != null) {
				quotes.put(symbol, price);
			}
		}
		return quotes;
	}
}
