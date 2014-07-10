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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fantasystocks.adapter.PortfolioAdapter;
import com.fantasystocks.model.Pool;
import com.fantasystocks.model.Portfolio;
import com.fantasystocks.model.Quote;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ViewPoolActivity extends Activity implements OnItemClickListener {

	private String poolId;

	private ListView lvPortfolios;
	private Button btnJoinPool;
	private PortfolioAdapter portfolioAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pool);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		lvPortfolios = (ListView) findViewById(R.id.lvPortfolios);
		btnJoinPool = (Button) findViewById(R.id.btnJoinPool);

		poolId = getIntent().getStringExtra("poolId");
		String poolName = getIntent().getStringExtra("poolName");
		String poolImageUrl = getIntent().getStringExtra("poolImageUrl");
		boolean canJoinPool = getIntent().getBooleanExtra("canJoinPool", false);

		getActionBar().setTitle(poolName);
		if (poolImageUrl != null) {
			getActionBar().setIcon(getResources().getIdentifier(poolImageUrl, "drawable", getPackageName()));
		}
		if (!canJoinPool) {
			btnJoinPool.setVisibility(View.GONE);
		}

		portfolioAdapter = new PortfolioAdapter(getBaseContext());
		lvPortfolios.setAdapter(portfolioAdapter);
		lvPortfolios.setOnItemClickListener(this);
		loadPortfolios(poolId);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Portfolio portfolio = portfolioAdapter.getItem(position);
		String userId = portfolio.getUser().getObjectId();
		Intent intent = new Intent(this, ViewPortfolioActivity.class);
		intent.putExtra("portfolioId", portfolio.getObjectId());
		intent.putExtra("portfolioName", portfolio.getUser().getString("name"));
		intent.putExtra("portfolioImageUrl", portfolio.getUser().getString("imageUrl"));
		intent.putExtra("canTrade", ParseUser.getCurrentUser().getObjectId().equals(userId));
		startActivity(intent);
	}

	public void onJoinPoolClicked(View view) {
		Pool pool = ParseObject.createWithoutData(Pool.class, poolId);
		pool.addPortfolio(ParseUser.getCurrentUser(), new SaveCallback() {
			@Override
			public void done(ParseException parseException) {
				if (parseException != null) {
					parseException.printStackTrace();
				}
				finish();
			}
		});
	}

	private void loadPortfolios(String poolId) {
		ParseQuery<Portfolio> query = ParseQuery.getQuery("Portfolio");
		query.whereEqualTo("pool", ParseObject.createWithoutData("Pool", poolId));
		query.include("user");
		query.include("lots");
		query.findInBackground(new FindCallback<Portfolio>() {
			@Override
			public void done(List<Portfolio> results, ParseException parseException) {
				if (parseException == null) {
					sortPortfolios(results);
				} else {
					parseException.printStackTrace();
				}
			}
		});
	}

	public void sortPortfolios(final List<Portfolio> portfolios) {
		Set<String> symbols = Sets.newHashSet();
		for (Portfolio portfolio : portfolios) {
			symbols.addAll(portfolio.getSymbols());
		}

		RestApplication.getFinanceClient().fetchQuotes(symbols, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Map<String, Quote> quotes = Quote.fromJSONArray(response);

				Map<String, Double> values = Maps.newHashMap();
				for (Portfolio portfolio : portfolios) {
					Double currentValue = portfolio.getCurrentValue(quotes);
					values.put(portfolio.getObjectId(), currentValue);
				}

				List<Portfolio> sorted = orderByValues(portfolios, values);
				portfolioAdapter.clear();
				portfolioAdapter.addAll(sorted);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(getClass().getName(), String.format("error fetching quote for symbols"));
			}
		});
	}

	private List<Portfolio> orderByValues(List<Portfolio> portfolios, final Map<String, Double> values) {
		Ordering<Portfolio> byValueOrdering = new Ordering<Portfolio>() {
			@Override
			public int compare(Portfolio left, Portfolio right) {
				return -Double.compare(values.get(left.getObjectId()), values.get(right.getObjectId()));
			}
		};
		return byValueOrdering.nullsLast().sortedCopy(portfolios);
	}

}
