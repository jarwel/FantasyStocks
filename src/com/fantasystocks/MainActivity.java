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

import com.fantasystocks.adapter.EnrolledPoolArrayAdapter;
import com.fantasystocks.model.Pool;
import com.google.common.collect.Lists;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends Activity implements OnItemClickListener {

	private ListView lvPools;

	private EnrolledPoolArrayAdapter poolAdapter;
	private List<Pool> pools = Lists.newArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvPools = (ListView) findViewById(R.id.lvPools);
		poolAdapter = new EnrolledPoolArrayAdapter(this, pools);
		lvPools.setAdapter(poolAdapter);
		lvPools.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ParseQuery<Pool> query = ParseQuery.getQuery("Pool");
		query.whereEqualTo("players", ParseUser.getCurrentUser());
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Pool pool = poolAdapter.getItem(position);
		Intent intent = new Intent(this, ViewPoolActivity.class);
		intent.putExtra("pool", pool);
		startActivity(intent);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) { 
		case R.id.action_create:
			Intent intent = new Intent(this, CreatePoolActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_join:
			Intent intent2 = new Intent(this, JoinPoolActivity.class);
			startActivity(intent2);
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
}
