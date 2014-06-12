package com.fantasyfinance.service;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.fantasyfinance.client.YahooFinanceClient;
import com.fantasyfinance.model.Quote;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

public class QuoteService {
	private static QuoteService instance;

	private final Context context;
	private final Gson gson;
	private final Handler handler;
	private final Map<String, Optional<Quote>> quotes;

	private QuoteService(Context context) {
		this.context = context.getApplicationContext();
		this.gson = new Gson();
		this.quotes = Maps.newHashMap();
		this.handler = new Handler();
		handler.postDelayed(updateQuotesThread, 0);
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

	private Runnable updateQuotesThread = new Runnable() {
		public void run() {
			if (quotes.keySet().size() > 0) {
				logQuotes();
				YahooFinanceClient.getInstance(context).fetchQuotes(quotes.keySet(), new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						parseJsonResponse(response);
						handler.postDelayed(updateQuotesThread, 5000);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(getClass().getSimpleName(), "Error: " + error.getMessage());
						handler.postDelayed(updateQuotesThread, 5000);
					}
				});
			}
		}
	};

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

	private void logQuotes() {
		StringBuilder builder = new StringBuilder("Updating quotes:");
		for (String symbol : quotes.keySet()) {
			builder.append(" ").append(symbol);
		}
		Log.d(getClass().getSimpleName(), builder.toString());
	}

}
