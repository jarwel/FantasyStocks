package com.fantasyfinance.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fantasyfinance.model.Quote;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

public class QuoteService {
	private static QuoteService instance;

	private static final String URL_FORMAT = "http://query.yahooapis.com/v1/public/yql?q=%s&env=store://datatables.org/alltableswithkeys&format=json";
	private static final String QUOTE_QUERY_FORMAT = "select * from yahoo.finance.quotes where symbol in (%s)";

	private final RequestQueue requestQueue;
	private final Gson gson;
	private final Handler handler;
	private final Map<String, Optional<Quote>> quotes;

	private QuoteService(Context context) {
		this.requestQueue = Volley.newRequestQueue(context);
		this.gson = new Gson();
		this.quotes = Maps.newHashMap();
		this.handler = new Handler();
		handler.postDelayed(updateTimerThread, 0);
	}

	public static QuoteService getInstance(Context context) {
		if (instance == null) {
			instance = new QuoteService(context.getApplicationContext());
		}
		return instance;
	}

	public Optional<Quote> getQuote(String symbol) {
		Optional<Quote> quote = quotes.get(symbol);
		if (quote == null) {
			quotes.put(symbol, Optional.<Quote> absent());
		}
		return quote;
	}

	private Runnable updateTimerThread = new Runnable() {
		public void run() {
			logQuotes();
			updateQuotes();
			handler.postDelayed(this, 5000);
		}
	};

	private void updateQuotes() {
		if (quotes.keySet().size() > 0) {
			try {
				String query = String.format(QUOTE_QUERY_FORMAT, stringFromSymbols(quotes.keySet()));
				String request = String.format(URL_FORMAT, URLEncoder.encode(query, Charsets.UTF_8.name()));
				requestQueue.add(new JsonObjectRequest(Method.GET, request, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// Log.d(getClass().getSimpleName(),
						// response.toString());
						parseJsonResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(getClass().getSimpleName(), "Error: " + error.getMessage());
					}
				}));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	private void parseJsonResponse(JSONObject response) {
		JSONObject queryObject = response.optJSONObject("query");
		if (queryObject != null) {
			JSONObject resultsObject = queryObject.optJSONObject("results");
			if (resultsObject != null) {

				JSONArray quotesArray = resultsObject.optJSONArray("quote");
				if (quotesArray == null) {
					quotesArray = new JSONArray();
					quotesArray.put(resultsObject.optJSONObject("quote"));
				}

				for (int i = 0; i < quotesArray.length(); i++) {
					JSONObject quoteObject = quotesArray.optJSONObject(i);
					if (quoteObject != null) {
						Quote quote = gson.fromJson(quoteObject.toString(), Quote.class);
						quotes.put(quote.getSymbol(), Optional.of(quote));
					}
				}
			}
		}
	}

	private static String stringFromSymbols(Set<String> symbols) {
		return Joiner.on(",").join(Iterables.transform(symbols, new Function<String, String>() {
			@Override
			public String apply(String symbol) {
				return String.format("'%s'", symbol);
			}
		}));
	}

	private void logQuotes() {
		StringBuilder builder = new StringBuilder("Updating quotes:");
		for (String symbol : quotes.keySet()) {
			builder.append(" ").append(symbol);
		}
		Log.d(getClass().getSimpleName(), builder.toString());
	}

}
