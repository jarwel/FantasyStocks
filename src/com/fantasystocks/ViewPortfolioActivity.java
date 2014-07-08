package com.fantasystocks;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.fantasystocks.adapter.LotAdapter;
import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Portfolio;
import com.google.common.collect.Lists;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewPortfolioActivity extends Activity {

	private String portfolioId;

	private ListView lvLots;
	private LotAdapter lotAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_portfolio);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		lvLots = (ListView) findViewById(R.id.lvLots);

		portfolioId = getIntent().getStringExtra("portfolioId");
		String portfolioName = getIntent().getStringExtra("portfolioName");
		String portfolioImageUrl = getIntent().getStringExtra("portfolioImageUrl");

		getActionBar().setTitle(String.format("%s's Portfolio", portfolioName));
		if (portfolioImageUrl != null) {
			getActionBar().setIcon(getResources().getIdentifier(portfolioImageUrl, "drawable", getPackageName()));
		}

		lotAdapter = new LotAdapter(getBaseContext(), Lists.<Lot> newArrayList());
		lvLots.setAdapter(lotAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ParseQuery<Lot> query = ParseQuery.getQuery("Lot");
		query.whereEqualTo("portfolio", ParseObject.createWithoutData(Portfolio.class, portfolioId));
		query.findInBackground(new FindCallback<Lot>() {
			@Override
			public void done(List<Lot> results, ParseException parseException) {
				if (parseException == null) {
					lotAdapter.clear();
					lotAdapter.addAll(results);
				} else {
					parseException.getStackTrace();
				}
			}
		});
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

}
