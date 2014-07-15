package com.fantasystocks;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fantasystocks.model.Portfolio;
import com.fantasystocks.model.Quote;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class TradeActivity extends Activity {

	private TextView tvFundsAvailable;
	private TextView tvSecurityName;
	private TextView tvSecurityPrice;
	private TextView tvOrderTotalLabel;
	private TextView tvOrderTotal;
	private TextView tvSecuritySymbol;
	private EditText etOrderShares;
	private EditText etSecuritySymbol;
	private Button btnPlaceOrder;

	private Portfolio portfolio;
	private Quote quote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		tvFundsAvailable = (TextView) findViewById(R.id.tvFundsAvailable);
		tvSecurityName = (TextView) findViewById(R.id.tvSecurityName);
		tvSecuritySymbol = (TextView) findViewById(R.id.tvSecuritySymbol);
		tvSecurityPrice = (TextView) findViewById(R.id.tvSecurityPrice);
		tvOrderTotalLabel = (TextView) findViewById(R.id.tvOrderTotalLabel);
		tvOrderTotal = (TextView) findViewById(R.id.tvOrderTotal);
		etOrderShares = (EditText) findViewById(R.id.etOrderShares);
		etSecuritySymbol = (EditText) findViewById(R.id.etSecuritySymbol);
		btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);

		String portfolioId = getIntent().getStringExtra("portfolioId");
		loadPortfolio(portfolioId);
		setListeners();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onPlaceOrderClicked(View view) {
		String symbol = etSecuritySymbol.getText().toString();
		int shares = Integer.parseInt(etOrderShares.getText().toString());
		final double costBasis = shares * quote.getPrice();
		portfolio.addLot(symbol, shares, costBasis, new SaveCallback() {
			@Override
			public void done(ParseException parseException) {
				if (parseException != null) {
					parseException.printStackTrace();
				} else {
					portfolio.setCash(portfolio.getCash() - costBasis);
					portfolio.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException parseException) {
							if (parseException != null) {
								parseException.printStackTrace();
							}
							finish();
						}
					});
				}
			}
		});
	}

	private void loadPortfolio(String portfolioId) {
		ParseQuery<Portfolio> query = ParseQuery.getQuery("Portfolio");
		query.getInBackground(portfolioId, new GetCallback<Portfolio>() {
			public void done(Portfolio result, ParseException parseException) {
				if (parseException == null) {
					portfolio = result;
					tvFundsAvailable.setText(RestApplication.getFormatter().formatCurrency(portfolio.getCash()));
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
			RestApplication.getFinanceClient().fetchQuote(symbol, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					Quote newQuote = Quote.fromJSONObject(response);
					if (newQuote != null && etSecuritySymbol.getText().toString().equals(newQuote.getSymbol())) {
						quote = newQuote;
						tvSecurityName.setText(quote.getName());
						tvSecuritySymbol.setText(quote.getSymbol());
						tvSecurityPrice.setText(RestApplication.getFormatter().formatCurrency(quote.getPrice()));
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
		tvOrderTotalLabel.setText("");
		tvOrderTotal.setText("");
		btnPlaceOrder.setEnabled(false);
		try {
			String orderNum = etOrderShares.getText().toString();
			if (!orderNum.isEmpty()) {
				int shares = Integer.parseInt(etOrderShares.getText().toString());
				double total = quote.getPrice() * shares;
				tvOrderTotal.setText(RestApplication.getFormatter().formatCurrency(total));
				if (total <= portfolio.getCash()) {
					tvOrderTotal.setTextColor(getResources().getColor(R.color.text_gray));
					tvOrderTotalLabel.setText(R.string.order_total_label);
					tvOrderTotalLabel.setTextColor(getResources().getColor(R.color.text_gray));
					btnPlaceOrder.setEnabled(true);
				} else {
					tvOrderTotal.setTextColor(getResources().getColor(R.color.text_red));
					tvOrderTotalLabel.setText(R.string.insufficient_funds_label);
					tvOrderTotalLabel.setTextColor(getResources().getColor(R.color.text_red));
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void setListeners() {
		etSecuritySymbol.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Do nothing
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Do nothing
			}

			@Override
			public void afterTextChanged(Editable s) {
				fetchQuote();
			}
		});
		etOrderShares.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Do nothing
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Do nothing
			}

			@Override
			public void afterTextChanged(Editable s) {
				calculateTotals();
			}
		});
	}

}
