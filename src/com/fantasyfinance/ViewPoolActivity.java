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

import com.fantasyfinance.model.Player;

public class ViewPoolActivity extends Activity implements OnItemClickListener {

	private List<Player> players;

	private ListView lvPlayers;
	private ArrayAdapter<Player> playersAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pool);
		lvPlayers = (ListView) findViewById(R.id.lvPlayers);
		players = Player.getMockPlayers();
		playersAdapter = new ArrayAdapter<Player>(getBaseContext(), android.R.layout.simple_list_item_1, players);
		lvPlayers.setAdapter(playersAdapter);
		lvPlayers.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(ViewPoolActivity.this, ViewPortfolioActivity.class);
		startActivity(intent);
	}
}
