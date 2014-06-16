package com.fantasystocks;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.fantasystocks.model.Pool;
import com.google.common.collect.Lists;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class JoinPoolActivity extends Activity {

	private ListView lvPools;

	private PoolArrayAdapter poolAdapter;
	private List<Pool> pools = Lists.newArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_pool);
		lvPools = (ListView) findViewById(R.id.lvPools);
		poolAdapter = new PoolArrayAdapter(this, pools);
		lvPools.setAdapter(poolAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ParseQuery<Pool> query = ParseQuery.getQuery(Pool.class);
		query.findInBackground(new FindCallback<Pool>() {

			@Override
			public void done(List<Pool> results, ParseException parseException) {
				if (parseException == null) {
					poolAdapter.clear();
					poolAdapter.addAll(results);
				}
			}
		});
	}

	public void onCreatePoolClicked(View view) {
		Intent intent = new Intent(JoinPoolActivity.this, CreatePoolActivity.class);
		startActivity(intent);
	}
}
