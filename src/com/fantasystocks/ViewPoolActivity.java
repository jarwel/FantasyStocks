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
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ViewPoolActivity extends Activity implements OnItemClickListener {

	private List<Player> players;
	private Pool pointer = new Pool();

	private TextView tvPoolName;
	private ListView lvPlayers;
	private PlayerAdapter playerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pool);
		String poolId = getIntent().getStringExtra("poolId");
		String poolName = getIntent().getStringExtra("poolName");

		pointer.setObjectId(poolId);

		tvPoolName = (TextView) findViewById(R.id.tvPoolName);
		lvPlayers = (ListView) findViewById(R.id.lvPlayers);

		tvPoolName.setText(poolName);

		players = Lists.newArrayList();
		playerAdapter = new PlayerAdapter(getBaseContext(), players);
		lvPlayers.setAdapter(playerAdapter);
		lvPlayers.setOnItemClickListener(this);
		loadPlayers();
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
			player.setPool(pointer);
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
		Intent intent = new Intent(ViewPoolActivity.this, ViewPlayerActivity.class);
		startActivity(intent);
	}

	private void loadPlayers() {
		ParseQuery<Player> query = ParseQuery.getQuery("Player");
		query.whereEqualTo("pool", pointer);
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
