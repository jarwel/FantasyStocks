package com.fantasystocks;

import java.text.DecimalFormat;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fantasystocks.client.YahooFinanceClient;
import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Player;
import com.fantasystocks.model.Quote;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class TradeActivity extends Activity {
	private static final DecimalFormat dollarFormat = new DecimalFormat("$##,##,##,##,##,##,##0.00");

	private Player player;
	private Quote quote;

	private TextView tvCash;
	private TextView tvSecurityName;
	private TextView tvSecurityPrice;
	private TextView tvOrderTotal;
	private TextView tvSecuritySymbol;
	private EditText etOrderShares;
	private EditText etSecuritySymbol;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade);
		tvCash = (TextView) findViewById(R.id.tvCash);
		tvSecurityName = (TextView) findViewById(R.id.tvSecurityName);
		tvSecuritySymbol = (TextView) findViewById(R.id.tvSecuritySymbol);
		tvSecurityPrice = (TextView) findViewById(R.id.tvSecurityPrice);
		tvOrderTotal = (TextView) findViewById(R.id.tvOrderTotal);
		etOrderShares = (EditText) findViewById(R.id.etOrderShares);
		etSecuritySymbol = (EditText) findViewById(R.id.etSecuritySymbol);
		etSecuritySymbol.requestFocus();

		String playerId = getIntent().getStringExtra("playerId");
		loadPlayer(playerId);
		setListeners();
	}

	public void onPlaceOrderClicked(View view) {
		String symbol = etSecuritySymbol.getText().toString();
		int shares = Integer.parseInt(etOrderShares.getText().toString());
		double costBasis = shares * quote.getPrice();

		Lot lot = new Lot();
		lot.setPlayer(player);
		lot.setSymbol(symbol);
		lot.setShares(shares);
		lot.setCostBasis(costBasis);
		lot.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException parseException) {
				if (parseException != null) {
					parseException.printStackTrace();
				}
				finish();
			}
		});
	}

	private void loadPlayer(String playerId) {
		ParseQuery<Player> query = ParseQuery.getQuery("Player");
		query.getInBackground(playerId, new GetCallback<Player>() {
			public void done(Player result, ParseException parseException) {
				if (parseException == null) {
					player = result;
					tvCash.setText(dollarFormat.format(player.getCash()));
				} else {
					parseException.printStackTrace();
				}
			}
		});
	}

	private void fetchQuote() {
		final String symbol = etSecuritySymbol.getText().toString();
		tvSecurityName.setText("");
		tvSecurityPrice.setText("");
		tvSecuritySymbol.setText("");
		if (symbol != null && !symbol.isEmpty()) {
			YahooFinanceClient.getInstance(this).fetchQuote(symbol, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					Quote newQuote = Quote.fromJSONObject(response);
					if (newQuote != null && etSecuritySymbol.getText().toString().equals(newQuote.getSymbol())) {
						quote = newQuote;
						tvSecurityName.setText(quote.getName());
						tvSecuritySymbol.setText(quote.getSymbol());
						tvSecurityPrice.setText(dollarFormat.format(quote.getPrice()));
					}
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
		tvOrderTotal.setText("");
		try {
			String orderNum = etOrderShares.getText().toString();
			if (!orderNum.isEmpty()) {
				int shares = Integer.parseInt(etOrderShares.getText().toString());
				double total = quote.getPrice() * shares;
				tvOrderTotal.setText(dollarFormat.format(total));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void setListeners() {
		etSecuritySymbol.addTextChangedListener(new TextWatcher() {
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
		etOrderShares.addTextChangedListener(new TextWatcher() {
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
