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

public class MainActivity extends Activity implements OnItemClickListener {

	private List<Pool> pools;

	private ListView lvPools;
	private ArrayAdapter<Pool> poolsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvPools = (ListView) findViewById(R.id.lvPools);
		pools = Pool.getMockPoolsWithPlace();
		poolsAdapter = new ArrayAdapter<Pool>(getBaseContext(), android.R.layout.simple_list_item_1, pools);
		lvPools.setAdapter(poolsAdapter);
		lvPools.setOnItemClickListener(this);
	}

	public void onJoinPoolClicked(View view) {
		Intent intent = new Intent(MainActivity.this, JoinPoolActivity.class);
		startActivity(intent);
	}
	
	public void launchLogin(View view) {
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Pool pool = pools.get(position);
		Intent intent = new Intent(MainActivity.this, ViewPoolActivity.class);
		startActivity(intent);
	}
}
