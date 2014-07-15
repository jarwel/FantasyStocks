package com.fantasystocks;

import java.util.Date;
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

import com.fantasystocks.adapter.HomeAdapter;
import com.fantasystocks.model.Portfolio;
import com.google.common.collect.Ordering;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class HomeActivity extends Activity implements OnItemClickListener {

	private ListView lvPools;
	private HomeAdapter homeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		lvPools = (ListView) findViewById(R.id.lvPools);
		homeAdapter = new HomeAdapter(getBaseContext());
		lvPools.setAdapter(homeAdapter);
		lvPools.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadPortfolios();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_create:
			Intent createIntent = new Intent(this, CreatePoolActivity.class);
			startActivity(createIntent);
			return true;
		case R.id.action_join:
			Intent findIntent = new Intent(this, FindPoolActivity.class);
			startActivity(findIntent);
			return true;
		case R.id.action_logout:
			ParseUser.logOut();
			Intent i = new Intent(this, LoginActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Portfolio portfolio = homeAdapter.getItem(position);
		Intent intent = new Intent(this, ViewPoolActivity.class);
		intent.putExtra("poolId", portfolio.getPool().getObjectId());
		intent.putExtra("poolName", portfolio.getPool().getName());
		intent.putExtra("poolImageUrl", portfolio.getPool().getPoolImageUrl());
		startActivity(intent);
	}

	private void loadPortfolios() {
		ParseQuery<Portfolio> query = ParseQuery.getQuery("Portfolio");
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.include("pool");
		query.include("lots");
		query.findInBackground(new FindCallback<Portfolio>() {
			@Override
			public void done(List<Portfolio> portfolios, ParseException parseException) {
				if (parseException == null) {
					List<Portfolio> sorted = sortPortfolios(portfolios);
					homeAdapter.clear();
					homeAdapter.addAll(sorted);
				} else {
					parseException.printStackTrace();
				}
			}
		});
	}

	private List<Portfolio> sortPortfolios(List<Portfolio> portfolios) {
		Ordering<Portfolio> byEndDateOrdering = new Ordering<Portfolio>() {
			@Override
			public int compare(Portfolio left, Portfolio right) {
				Date leftDate = left.getPool().getEndDate();
				Date rightDate = right.getPool().getEndDate();
				return leftDate.compareTo(rightDate);
			}
		};
		return byEndDateOrdering.sortedCopy(portfolios);
	}
}
