package com.fantasystocks;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.fantasystocks.adapter.PoolAdapter;
import com.fantasystocks.model.Pool;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FindPoolActivity extends Activity implements OnItemClickListener, OnQueryTextListener {

	private ListView lvPools;
	private SearchView svPool;
	private PoolAdapter poolAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_pool);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		lvPools = (ListView) findViewById(R.id.lvPools);
		svPool = (SearchView) findViewById(R.id.svPool);
		poolAdapter = new PoolAdapter(getBaseContext());

		lvPools.setAdapter(poolAdapter);
		lvPools.setOnItemClickListener(this);
		svPool.setOnQueryTextListener(this);

		AutoCompleteTextView searchText = (AutoCompleteTextView) svPool.findViewById(svPool.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null));
		searchText.setHintTextColor(Color.parseColor("#AAAAAA"));
		searchText.setTextSize(14);
	}

	@Override
	protected void onResume() {
		super.onResume();
		fetchPoolResults("");
	}

	public void fetchPoolResults(String queryString) {
		ParseQuery<Pool> query = ParseQuery.getQuery(Pool.class);
		query.whereContains("canonicalName", queryString.toLowerCase(Locale.getDefault()));
		query.whereNotContainedIn("players", Lists.newArrayList(ParseUser.getCurrentUser()));
		query.findInBackground(new FindCallback<Pool>() {
			@Override
			public void done(List<Pool> results, ParseException parseException) {
				if (parseException == null) {
					poolAdapter.clear();
					poolAdapter.addAll(Lists.newArrayList(Iterables.filter(results, new Predicate<Pool>() {
						@Override
						public boolean apply(Pool pool) {
							return pool.getPlayerCount() < pool.getPlayerLimit();
						}
					})));
				} else {
					parseException.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Pool pool = poolAdapter.getItem(position);
		Intent intent = new Intent(this, ViewPoolActivity.class);
		intent.putExtra("poolId", pool.getObjectId());
		intent.putExtra("poolName", pool.getName());
		intent.putExtra("poolImageUrl", pool.getPoolImageUrl());
		intent.putExtra("canJoin", true);
		startActivity(intent);
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
	public boolean onQueryTextSubmit(String query) {
		fetchPoolResults(query);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return true;
	}
}