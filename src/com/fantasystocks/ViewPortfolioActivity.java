package com.fantasystocks;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fantasystocks.adapter.LotAdapter;
import com.fantasystocks.model.Portfolio;
import com.fantasystocks.model.Quote;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ViewPortfolioActivity extends Activity {

	private TextView tvRank;
	private TextView tvPoolNameInPortfolio;
	private TextView tvCurrentValue;
	private TextView tvValueChange;
	private TextView tvPercentChange;
	private TextView tvCash;
	private ImageView ivPoolImageInPortfolio;
	private ListView lvLots;
	private Button btnTrade;
	private LotAdapter lotAdapter;

	private String portfolioId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_portfolio);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		tvRank = (TextView) findViewById(R.id.tvRank);
		tvPoolNameInPortfolio = (TextView) findViewById(R.id.tvPoolNameInPortfolio);
		tvCurrentValue = (TextView) findViewById(R.id.tvCurrentValue);
		tvValueChange = (TextView) findViewById(R.id.tvValueChange);
		tvPercentChange = (TextView) findViewById(R.id.tvPercentChange);
		tvCash = (TextView) findViewById(R.id.tvCash);
		ivPoolImageInPortfolio = (ImageView) findViewById(R.id.ivPoolImageInPortfolio);
		lvLots = (ListView) findViewById(R.id.lvLots);
		btnTrade = (Button) findViewById(R.id.btnTrade);

		portfolioId = getIntent().getStringExtra("portfolioId");
		String portfolioName = getIntent().getStringExtra("portfolioName");
		int poolRank = getIntent().getIntExtra("portfolioRank", 0);

		getActionBar().setTitle(String.format("%s's Portfolio", portfolioName));
		tvRank.setText(String.format("%s", RestApplication.getFormatter().formatRank(poolRank)));

		lotAdapter = new LotAdapter(getBaseContext());
		lvLots.setAdapter(lotAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadPortfolio();
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

	private void loadPortfolio() {
		ParseQuery<Portfolio> query = ParseQuery.getQuery("Portfolio");
		query.include("user");
		query.include("pool");
		query.include("lots");
		query.getInBackground(portfolioId, new GetCallback<Portfolio>() {
			public void done(Portfolio portfolio, ParseException parseException) {
				if (parseException == null) {
					tvPoolNameInPortfolio.setText(portfolio.getPool().getName());
					int photoMediaUrl = getResources().getIdentifier(portfolio.getPool().getPoolImageUrl(), "drawable", getPackageName());
					ivPoolImageInPortfolio.setImageResource(photoMediaUrl);

					lotAdapter.clear();
					lotAdapter.addAll(portfolio.getLots());
					loadQuotes(portfolio);

					if (ParseUser.getCurrentUser().getObjectId().equals(portfolio.getUser().getObjectId())) {
						btnTrade.setVisibility(View.VISIBLE);
					}
				} else {
					parseException.printStackTrace();
				}
			}
		});
	}

	private void loadQuotes(final Portfolio portfolio) {
		Set<String> symbols = portfolio.getSymbols();

		if (symbols.isEmpty()) {
			tvCurrentValue.setText(RestApplication.getFormatter().formatCurrency(portfolio.getCash()));
			tvCash.setText(RestApplication.getFormatter().formatCurrency(portfolio.getCash()));
		} else {

			RestApplication.getFinanceClient().fetchQuotes(symbols, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					Map<String, Quote> quotes = Quote.fromJSONArray(response);
					Double currentValue = portfolio.getCurrentValue(quotes);
					if (currentValue != null) {
						double valueChange = currentValue - portfolio.getStartingFunds();
						double percentChange = valueChange / portfolio.getStartingFunds();
						tvCurrentValue.setText(RestApplication.getFormatter().formatCurrency(currentValue));
						tvCash.setText(RestApplication.getFormatter().formatCurrency(portfolio.getCash()));
						tvValueChange.setText(RestApplication.getFormatter().formatChange(valueChange));
						tvValueChange.setTextColor(RestApplication.getFormatter().getColorResource(valueChange));
						tvPercentChange.setText(RestApplication.getFormatter().formatPercent(percentChange));
						tvPercentChange.setTextColor(RestApplication.getFormatter().getColorResource(valueChange));
					}
				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Log.e(getClass().getName(), String.format("error fetching quote for symbols"));
				}
			});
		}
	}
}
