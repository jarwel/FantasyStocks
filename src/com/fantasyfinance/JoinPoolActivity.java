package com.fantasyfinance;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fantasyfinance.model.Pool;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class JoinPoolActivity extends Activity {

	private ListView lvPools;
	private ArrayAdapter<Pool> poolsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_pool);
		lvPools = (ListView) findViewById(R.id.lvPools);
		poolsAdapter = new ArrayAdapter<Pool>(getBaseContext(), android.R.layout.simple_list_item_1);
		lvPools.setAdapter(poolsAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ParseQuery<Pool> query = ParseQuery.getQuery(Pool.class);
		query.findInBackground(new FindCallback<Pool>() {

			@Override
			public void done(List<Pool> results, ParseException parseException) {
				if (parseException != null) {
					poolsAdapter.addAll(results);
					poolsAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	public void onCreatePoolClicked(View view) {
		Intent intent = new Intent(JoinPoolActivity.this, CreatePoolActivity.class);
		startActivity(intent);
	}
}
