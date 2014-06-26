package com.fantasystocks;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Player;
import com.google.common.collect.Lists;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewPlayerActivity extends Activity {

	private String playerId;

	private ListView lvLots;
	private ArrayAdapter<Lot> lotsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_player);
		lvLots = (ListView) findViewById(R.id.lvLots);

		playerId = getIntent().getStringExtra("playerId");

		lotsAdapter = new ArrayAdapter<Lot>(getBaseContext(), android.R.layout.simple_list_item_1, Lists.<Lot> newArrayList());
		lvLots.setAdapter(lotsAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ParseQuery<Lot> query = ParseQuery.getQuery("Lot");
		query.whereEqualTo("player", ParseObject.createWithoutData(Player.class, playerId));
		query.findInBackground(new FindCallback<Lot>() {
			@Override
			public void done(List<Lot> results, ParseException parseException) {
				if (parseException == null) {
					lotsAdapter.clear();
					lotsAdapter.addAll(results);
				} else {
					parseException.getStackTrace();
				}
			}
		});
	}

	public void onTradeClicked(View view) {
		Intent intent = new Intent(ViewPlayerActivity.this, TradeActivity.class);
		intent.putExtra("playerId", playerId);
		startActivity(intent);
	}

}
