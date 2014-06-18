package com.fantasystocks;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fantasystocks.client.YahooFinanceClient;
import com.fantasystocks.model.Quote;
import com.google.common.collect.ImmutableSet;

public class TradeActivity extends Activity {

	private Quote quote;

	private EditText etSymbol;
	private EditText etShares;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade);

		etSymbol = (EditText) findViewById(R.id.etSymbol);
		etSymbol.requestFocus();

		etShares = (EditText) findViewById(R.id.etShares);

		setListeners();
	}

	private void fetchQuote() {
		final String symbol = etSymbol.getText().toString();
		if (symbol != null && !symbol.isEmpty()) {
			YahooFinanceClient.getInstance(this).fetchQuotes(ImmutableSet.of(symbol), new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject object) {

				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Log.e(getClass().getName(), String.format("error fetching quote for symbol %s", symbol));
				}
			});
		}
	}

	private void calculateTotals() {

	}

	private void setListeners() {
		etSymbol.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				fetchQuote();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				fetchQuote();
			}

			@Override
			public void afterTextChanged(Editable s) {
				fetchQuote();
			}
		});
		etShares.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				calculateTotals();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				calculateTotals();
			}

			@Override
			public void afterTextChanged(Editable s) {
				calculateTotals();
			}
		});
	}
}
