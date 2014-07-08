package com.fantasystocks;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fantasystocks.adapter.LotAdapter;
import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Portfolio;
import com.fantasystocks.model.Quote;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewPortfolioActivity extends Activity {

	private String portfolioId;
	private List<Lot> lots;
	private Set<String> symbols;

	private TextView tvCash;
	private ListView lvLots;
	private Button btnTrade;
	private LotAdapter lotAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_portfolio);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		tvCash = (TextView) findViewById(R.id.tvCash);
		lvLots = (ListView) findViewById(R.id.lvLots);
		btnTrade = (Button) findViewById(R.id.btnTrade);

		portfolioId = getIntent().getStringExtra("portfolioId");
		String portfolioName = getIntent().getStringExtra("portfolioName");
		String portfolioImageUrl = getIntent().getStringExtra("portfolioImageUrl");
		double cash = getIntent().getDoubleExtra("portfolioCash", 0);
		boolean canTrade = getIntent().getBooleanExtra("canTrade", false);

		getActionBar().setTitle(String.format("%s's Portfolio", portfolioName));
		if (portfolioImageUrl != null) {
			getActionBar().setIcon(getResources().getIdentifier(portfolioImageUrl, "drawable", getPackageName()));
		}
		if (!canTrade) {
			btnTrade.setVisibility(View.GONE);
		}
		tvCash.setText(RestApplication.dollarFormat.format(cash));

		lots = Lists.newArrayList();
		lotAdapter = new LotAdapter(getBaseContext(), lots);
		lvLots.setAdapter(lotAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadLots();
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

	public void onTradeClicked(View view) {
		Intent intent = new Intent(ViewPortfolioActivity.this, TradeActivity.class);
		intent.putExtra("portfolioId", portfolioId);
		startActivity(intent);
	}

	private void loadLots() {
		ParseQuery<Lot> query = ParseQuery.getQuery("Lot");
		query.whereEqualTo("portfolio", ParseObject.createWithoutData(Portfolio.class, portfolioId));
		query.findInBackground(new FindCallback<Lot>() {
			@Override
			public void done(List<Lot> results, ParseException parseException) {
				if (parseException == null) {
					lotAdapter.clear();
					lotAdapter.addAll(results);
					symbols = Sets.newHashSet(Iterables.transform(results, new Function<Lot, String>() {
						@Override
						public String apply(Lot lot) {
							return lot.getSymbol();
						}
					}));
					loadQuotes();
				} else {
					parseException.getStackTrace();
				}
			}
		});
	}

	private void loadQuotes() {
		RestApplication.getFinanceClient().fetchQuotes(symbols, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Map<String, Quote> quotes = Maps.newHashMap();
				for (Quote quote : Quote.fromJSONArray(response)) {
					quotes.put(quote.getSymbol(), quote);
				}
				lotAdapter.setQuotes(quotes);
				lotAdapter.notifyDataSetChanged();
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(getClass().getName(), String.format("error fetching quote for symbols"));
			}
		});
	}

}
