package com.fantasystocks;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fantasystocks.adapter.OpenPoolArrayAdapter;
import com.fantasystocks.model.Pool;
import com.google.common.collect.Lists;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class JoinPoolActivity extends Activity implements OnItemClickListener {

	private ListView lvPools;

	private OpenPoolArrayAdapter poolAdapter;
	private List<Pool> pools = Lists.newArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_pool);
		lvPools = (ListView) findViewById(R.id.lvPools);
		poolAdapter = new OpenPoolArrayAdapter(this, pools);
		lvPools.setAdapter(poolAdapter);
		lvPools.setOnItemClickListener(this);
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
		intent.putExtra("pool", pool);
		startActivity(intent);
	}
}
