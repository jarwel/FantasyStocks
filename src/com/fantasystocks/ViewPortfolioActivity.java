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
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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

	@InjectView(R.id.tvRank)
	protected TextView tvRank;

	@InjectView(R.id.tvPoolNameInPortfolio)
	protected TextView tvPoolNameInPortfolio;

	@InjectView(R.id.tvCurrentValue)
	protected TextView tvCurrentValue;

	@InjectView(R.id.tvChange)
	protected TextView tvChange;

	@InjectView(R.id.tvCash)
	protected TextView tvCash;

	@InjectView(R.id.ivPoolImageInPortfolio)
	protected ImageView ivPoolImageInPortfolio;

	@InjectView(R.id.lvLots)
	protected ListView lvLots;

	@InjectView(R.id.btnTrade)
	protected Button btnTrade;

	private LotAdapter lotAdapter;

	private String portfolioId;
	private int rankInPool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_portfolio);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		ButterKnife.inject(this);

		portfolioId = getIntent().getStringExtra("portfolioId");
		rankInPool = getIntent().getIntExtra("portfolioRank", 0);
		String portfolioName = getIntent().getStringExtra("portfolioName");

		getActionBar().setTitle(String.format("%s's Portfolio", portfolioName));

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

	@OnClick(R.id.btnTrade)
	public void onTrade(View view) {
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

						tvRank.setText(String.format("%s", RestApplication.getFormatter().formatRank(rankInPool)));
						tvCurrentValue.setText(RestApplication.getFormatter().formatCurrency(currentValue));
						tvCash.setText(RestApplication.getFormatter().formatCurrency(portfolio.getCash()));

						String valueChangeFormat = RestApplication.getFormatter().formatChange(valueChange);
						String percentChangeFormat = RestApplication.getFormatter().formatPercent(percentChange);
						tvChange.setText(String.format("%s (%s)", valueChangeFormat, percentChangeFormat));
						tvChange.setTextColor(RestApplication.getFormatter().getColorResource(valueChange));
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
