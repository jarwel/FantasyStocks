package com.fantasyfinance.client;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

public class YahooFinanceClient {
	private static YahooFinanceClient instance;

	private static final String URL_FORMAT = "http://query.yahooapis.com/v1/public/yql?q=%s&env=store://datatables.org/alltableswithkeys&format=json";
	private static final String QUOTE_QUERY_FORMAT = "select * from yahoo.finance.quotes where symbol in (%s)";

	public static YahooFinanceClient getInstance() {
		if (instance == null) {
			instance = new YahooFinanceClient();
		}
		return instance;
	}

	public Map<String, Double> fetchQuotes(Set<String> symbols) {
		Joiner onCommanJoiner = Joiner.on(",").skipNulls();
		String values = onCommanJoiner.join(Iterables.transform(symbols, new Function<String, String>() {
			@Override
			public String apply(String symbol) {
				return String.format("'%s'", symbol);
			}
		}));
		String url = String.format(URL_FORMAT, String.format(QUOTE_QUERY_FORMAT, values));
		return Maps.newHashMap();
	}
}
