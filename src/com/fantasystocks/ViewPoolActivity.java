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
import android.widget.TextView;

import com.fantasystocks.adapter.PlayerAdapter;
import com.fantasystocks.model.Player;
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
	private List<Player> players;

	private TextView tvPoolName;
	private ListView lvPlayers;
	private PlayerAdapter playerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pool);
		tvPoolName = (TextView) findViewById(R.id.tvPoolName);
		lvPlayers = (ListView) findViewById(R.id.lvPlayers);

		poolId = getIntent().getStringExtra("poolId");
		String poolName = getIntent().getStringExtra("poolName");

		tvPoolName.setText(poolName);

		players = Lists.newArrayList();
		playerAdapter = new PlayerAdapter(getBaseContext(), players);
		lvPlayers.setAdapter(playerAdapter);
		lvPlayers.setOnItemClickListener(this);
		loadPlayers(poolId);
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
			Player player = new Player();
			player.setUser(ParseUser.getCurrentUser());
			player.setPool(ParseObject.createWithoutData(Pool.class, poolId));
			player.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException parseException) {
					if (parseException != null) {
						parseException.printStackTrace();
					}
					finish();
				}
			});
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Player player = playerAdapter.getItem(position);
		Intent intent = new Intent(ViewPoolActivity.this, ViewPlayerActivity.class);
		intent.putExtra("playerId", player.getObjectId());
		startActivity(intent);
	}

	private void loadPlayers(String poolId) {
		ParseQuery<Player> query = ParseQuery.getQuery("Player");
		query.whereEqualTo("pool", ParseObject.createWithoutData("Pool", poolId));
		query.include("user");
		query.findInBackground(new FindCallback<Player>() {
			@Override
			public void done(List<Player> results, ParseException parseException) {
				if (parseException == null) {
					playerAdapter.clear();
					playerAdapter.addAll(results);
				} else {
					parseException.printStackTrace();
				}
			}
		});
	}

}
