package com.fantasystocks;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fantasystocks.adapter.PortfolioAdapter;
import com.fantasystocks.model.Portfolio;
import com.fantasystocks.model.Pool;
import com.google.common.collect.Lists;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ViewPoolActivity extends Activity implements OnItemClickListener {

	private String poolId;
	private List<Portfolio> portfolios;

	private ListView lvPortfolios;
	private PortfolioAdapter portfolioAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pool);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		lvPortfolios = (ListView) findViewById(R.id.lvPortfolios);

		poolId = getIntent().getStringExtra("poolId");
		String poolName = getIntent().getStringExtra("poolName");
		String poolImageUrl = getIntent().getStringExtra("poolImageUrl");

		getActionBar().setTitle(poolName);
		if (poolImageUrl != null) {
			getActionBar().setIcon(getResources().getIdentifier(poolImageUrl, "drawable", getPackageName()));
		}

		portfolios = Lists.newArrayList();
		portfolioAdapter = new PortfolioAdapter(getBaseContext(), portfolios);
		lvPortfolios.setAdapter(portfolioAdapter);
		lvPortfolios.setOnItemClickListener(this);
		loadPortfolios(poolId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_view_pool, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_join_pool:
			Portfolio portfolio = new Portfolio();
			portfolio.setUser(ParseUser.getCurrentUser());
			portfolio.setPool(ParseObject.createWithoutData(Pool.class, poolId));
			portfolio.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException parseException) {
					if (parseException != null) {
						parseException.printStackTrace();
					}
					finish();
				}
			});
			return true;
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
		startActivity(intent);
	}

	private void loadPortfolios(String poolId) {
		ParseQuery<Portfolio> query = ParseQuery.getQuery("Portfolio");
		query.whereEqualTo("pool", ParseObject.createWithoutData("Pool", poolId));
		query.include("user");
		query.findInBackground(new FindCallback<Portfolio>() {
			@Override
			public void done(List<Portfolio> results, ParseException parseException) {
				if (parseException == null) {
					portfolioAdapter.clear();
					portfolioAdapter.addAll(results);
				} else {
					parseException.printStackTrace();
				}
			}
		});
	}

}
