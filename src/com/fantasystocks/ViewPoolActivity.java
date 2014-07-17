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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ViewPoolActivity extends Activity implements OnItemClickListener {

	private TextView tvPoolDates;
	private TextView tvPoolStatus;
	private TextView tvPoolPlayers;
	private TextView tvPoolAvailableFunds;
	private TextView tvPoolName;
	private ListView lvPoolPortfolios;
	private ImageView ivPoolImage;
	private Button btnJoinPool;
	private PortfolioAdapter portfolioAdapter;

	private String poolId;
	private boolean canJoin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pool);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		tvPoolDates = (TextView) findViewById(R.id.tvPoolDates);
		tvPoolStatus = (TextView) findViewById(R.id.tvPoolStatus);
		tvPoolPlayers = (TextView) findViewById(R.id.tvPoolPlayers);
		tvPoolName = (TextView) findViewById(R.id.tvPoolName);
		ivPoolImage = (ImageView) findViewById(R.id.ivPoolImage);
		tvPoolAvailableFunds = (TextView) findViewById(R.id.tvPoolStartingFunds);
		lvPoolPortfolios = (ListView) findViewById(R.id.lvPoolPortfolios);
		btnJoinPool = (Button) findViewById(R.id.btnJoinPool);

		poolId = getIntent().getStringExtra("poolId");
		canJoin = getIntent().getBooleanExtra("canJoin", false);
		String poolName = getIntent().getStringExtra("poolName");

		getActionBar().setTitle(poolName);

		portfolioAdapter = new PortfolioAdapter(getBaseContext());
		lvPoolPortfolios.setAdapter(portfolioAdapter);
		lvPoolPortfolios.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadPool(poolId);
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
		Intent intent = new Intent(this, ViewPortfolioActivity.class);
		intent.putExtra("portfolioId", portfolio.getObjectId());
		intent.putExtra("portfolioName", portfolio.getUser().getString("name"));
		intent.putExtra("portfolioImageUrl", portfolio.getUser().getString("imageUrl"));
		intent.putExtra("portfolioRank", position + 1);
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
				launchMainActivity();
			}
		});
	}

	public void launchMainActivity() {
		Intent i = new Intent(this, HomeActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}

	private void loadPool(String poolId) {
		ParseQuery<Pool> query = ParseQuery.getQuery("Pool");
		query.getInBackground(poolId, new GetCallback<Pool>() {
			@Override
			public void done(Pool pool, ParseException parseException) {
				if (parseException == null) {
					String formattedStartDate = RestApplication.getFormatter().formatShortDate(pool.getStartDate());
					String formattedEndDate = RestApplication.getFormatter().formatShortDate(pool.getEndDate());

					tvPoolDates.setText(String.format("%s - %s", formattedStartDate, formattedEndDate));
					tvPoolName.setText(pool.getName());
					tvPoolAvailableFunds.setText(RestApplication.getFormatter().formatCurrency(pool.getFunds()));

					int photoMediaUrl = getResources().getIdentifier(pool.getPoolImageUrl(), "drawable", getPackageName());
					ivPoolImage.setImageResource(photoMediaUrl);

					if (pool.isOpen()) {
						tvPoolStatus.setText(R.string.status_open_label);
						tvPoolStatus.setTextColor(getResources().getColor(R.color.text_green));
						if (canJoin) {
							btnJoinPool.setVisibility(View.VISIBLE);
						}
						tvPoolPlayers.setText(String.format("%d (%d Open)", pool.getPlayerCount(), pool.getOpenCount()));
					} else {
						tvPoolStatus.setText(R.string.status_closed_label);
						tvPoolStatus.setTextColor(getResources().getColor(R.color.text_red));
						tvPoolPlayers.setText(String.format("%d", pool.getPlayerCount()));
					}
				} else {
					parseException.printStackTrace();
				}
			}
		});
	}

	private void loadPortfolios(String poolId) {
		ParseQuery<Portfolio> query = ParseQuery.getQuery("Portfolio");
		query.whereEqualTo("pool", ParseObject.createWithoutData(Pool.class, poolId));
		query.include("user");
		query.include("lots");
		query.findInBackground(new FindCallback<Portfolio>() {
			@Override
			public void done(List<Portfolio> results, ParseException parseException) {
				if (parseException == null) {
					loadQuotes(results);
				} else {
					parseException.printStackTrace();
				}
			}
		});
	}

	private void loadQuotes(final List<Portfolio> portfolios) {
		Set<String> symbols = Sets.newHashSet();
		for (Portfolio portfolio : portfolios) {
			symbols.addAll(portfolio.getSymbols());
		}

		if (symbols.isEmpty()) {
			portfolioAdapter.clear();
			portfolioAdapter.addAll(portfolios);
		} else {
			RestApplication.getFinanceClient().fetchQuotes(symbols, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					Map<String, Quote> quotes = Quote.fromJSONArray(response);
					List<Portfolio> sorted = orderByCurrentValue(portfolios, quotes);
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
	}

	private List<Portfolio> orderByCurrentValue(List<Portfolio> portfolios, Map<String, Quote> quotes) {
		final Map<String, Double> values = Maps.newHashMap();
		for (Portfolio portfolio : portfolios) {
			Double currentValue = portfolio.getCurrentValue(quotes);
			values.put(portfolio.getObjectId(), currentValue);
		}

		Ordering<Portfolio> byValueOrdering = new Ordering<Portfolio>() {
			@Override
			public int compare(Portfolio left, Portfolio right) {
				return -Double.compare(values.get(left.getObjectId()), values.get(right.getObjectId()));
			}
		};
		return byValueOrdering.nullsLast().sortedCopy(portfolios);
	}

}
