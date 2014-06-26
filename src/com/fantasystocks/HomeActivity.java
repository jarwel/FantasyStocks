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

import com.fantasystocks.adapter.HomeAdapter;
import com.fantasystocks.model.Player;
import com.google.common.collect.Lists;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class HomeActivity extends Activity implements OnItemClickListener {

	private ListView lvPools;

	private HomeAdapter homeAdapter;
	private List<Player> players = Lists.newArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		lvPools = (ListView) findViewById(R.id.lvPools);
		homeAdapter = new HomeAdapter(this, players);
		lvPools.setAdapter(homeAdapter);
		lvPools.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ParseQuery<Player> query = ParseQuery.getQuery("Player");
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.include("pool");
		query.findInBackground(new FindCallback<Player>() {

			@Override
			public void done(List<Player> results, ParseException parseException) {
				if (parseException == null) {
					homeAdapter.clear();
					homeAdapter.addAll(results);
				} else {
					parseException.printStackTrace();
				}
			}
		});
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Player player = homeAdapter.getItem(position);
		Intent intent = new Intent(this, ViewPoolActivity.class);
		intent.putExtra("poolId", player.getPool().getObjectId());
		intent.putExtra("poolName", player.getPool().getName());
		startActivity(intent);
	}
}
