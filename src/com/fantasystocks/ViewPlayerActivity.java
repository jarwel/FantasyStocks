package com.fantasystocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fantasystocks.model.Lot;
import com.fantasystocks.model.Player;
import com.google.common.collect.Lists;

public class ViewPlayerActivity extends Activity {

	private ListView lvLots;
	private ArrayAdapter<Lot> lotsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_player);
		lvLots = (ListView) findViewById(R.id.lvLots);
		Player portfolio = new Player();
		lotsAdapter = new ArrayAdapter<Lot>(getBaseContext(), android.R.layout.simple_list_item_1, Lists.<Lot> newArrayList());
		lvLots.setAdapter(lotsAdapter);
	}

	public void onTradeClicked(View view) {
		Intent intent = new Intent(ViewPlayerActivity.this, TradeActivity.class);
		startActivity(intent);
	}
}
