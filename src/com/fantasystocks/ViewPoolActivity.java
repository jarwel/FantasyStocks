package com.fantasystocks;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fantasystocks.adapter.PlayerArrayAdapter;
import com.fantasystocks.model.Player;
import com.fantasystocks.model.Pool;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class ViewPoolActivity extends Activity implements OnItemClickListener {

	private Pool pool;
	private List<Player> players;

	private TextView tvPoolName;
	private ListView lvPlayers;
	private ArrayAdapter<Player> playersAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pool);
		pool = (Pool) getIntent().getSerializableExtra("pool");

		tvPoolName = (TextView) findViewById(R.id.tvPoolName);
		lvPlayers = (ListView) findViewById(R.id.lvPlayers);

		players = Lists.newArrayList();
		playersAdapter = new PlayerArrayAdapter(getBaseContext(), players, pool);
		lvPlayers.setAdapter(playersAdapter);
		lvPlayers.setOnItemClickListener(this);
		tvPoolName.setText(pool.getName());
		loadPlayers();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(ViewPoolActivity.this, ViewPortfolioActivity.class);
		startActivity(intent);
	}

	private void loadPlayers() {
		/* Hack in order to update the UI */
		playersAdapter.clear();
		for (int i=0; i<10; i++) {
			Player p = new Player(ParseUser.getCurrentUser(), 10000.99);
			playersAdapter.add(p);
		}
		
		/*
		Log.v("DEBUG", "poolId: " + pool.getObjectId());
		ParseRelation<ParseUser> relation = pool.getRelation("players");
		relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> results, ParseException parseException) {
				if (parseException == null) {
					playersAdapter.clear();
					playersAdapter.addAll(Lists.transform(results, new Function<ParseUser, Player>() {
						@Override
						public Player apply(ParseUser parseUser) {
							return new Player(parseUser, 0);
						}
					}));
				} else {
					parseException.printStackTrace();
				}
			}
		});
	*/
	}
}
