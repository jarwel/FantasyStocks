package com.fantasystocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fantasystocks.adapter.LotAdapter;
import com.fantasystocks.model.Portfolio;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class ViewPortfolioActivity extends Activity {

	private String portfolioId;

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

		boolean canTrade = getIntent().getBooleanExtra("canTrade", false);

		getActionBar().setTitle(String.format("%s's Portfolio", portfolioName));
		if (portfolioImageUrl != null) {
			getActionBar().setIcon(getResources().getIdentifier(portfolioImageUrl, "drawable", getPackageName()));
		}
		if (!canTrade) {
			btnTrade.setVisibility(View.GONE);
		}

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
		query.include("lots");
		query.getInBackground(portfolioId, new GetCallback<Portfolio>() {
			public void done(Portfolio result, ParseException parseException) {
				if (parseException == null) {
					tvCash.setText(RestApplication.getFormatter().formatCurrency(result.getCash()));
					lotAdapter.clear();
					lotAdapter.addAll(result.getLots());
				} else {
					parseException.printStackTrace();
				}
			}
		});
	}
}
