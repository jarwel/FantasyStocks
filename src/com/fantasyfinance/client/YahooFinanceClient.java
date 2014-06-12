package com.fantasyfinance.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class YahooFinanceClient {
	private static YahooFinanceClient instance;

	private static final String URL_FORMAT = "http://query.yahooapis.com/v1/public/yql?q=%s&env=store://datatables.org/alltableswithkeys&format=json";
	private static final String QUOTE_QUERY_FORMAT = "select * from yahoo.finance.quotes where symbol in (%s)";

	private final RequestQueue requestQueue;

	public static YahooFinanceClient getInstance(Context context) {
		if (instance == null) {
			instance = new YahooFinanceClient(context.getApplicationContext());
		}
		return instance;
	}

	private YahooFinanceClient(Context context) {
		this.requestQueue = Volley.newRequestQueue(context);
	}

	public void fetchQuotes(Set<String> symbols, Listener<JSONObject> onSuccess, ErrorListener onError) {
		try {
			String query = String.format(QUOTE_QUERY_FORMAT, setToQueryString(symbols));
			String request = String.format(URL_FORMAT, URLEncoder.encode(query, Charsets.UTF_8.name()));
			requestQueue.add(new JsonObjectRequest(Method.GET, request, null, onSuccess, onError));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static String setToQueryString(Set<String> symbols) {
		return Joiner.on(",").join(Iterables.transform(symbols, new Function<String, String>() {
			@Override
			public String apply(String symbol) {
				return String.format("'%s'", symbol);
			}
		}));
	}
}
