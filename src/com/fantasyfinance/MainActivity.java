package com.fantasyfinance;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fantasyfinance.model.Pool;
import com.google.common.collect.Lists;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class MainActivity extends Activity implements OnItemClickListener {

	private ListView lvPools;
	private ArrayAdapter<Pool> poolsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvPools = (ListView) findViewById(R.id.lvPools);
		poolsAdapter = new ArrayAdapter<Pool>(getBaseContext(), android.R.layout.simple_list_item_1);
		lvPools.setAdapter(poolsAdapter);
		lvPools.setOnItemClickListener(this);
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

	public void onJoinPoolClicked(View view) {
		Intent intent = new Intent(MainActivity.this, JoinPoolActivity.class);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Pool pool = poolsAdapter.getItem(position);
		Intent intent = new Intent(MainActivity.this, ViewPoolActivity.class);
		startActivity(intent);
	}
}
