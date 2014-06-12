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
import com.fantasyfinance.service.QuoteService;
import com.parse.Parse;
import com.parse.ParseObject;

public class MainActivity extends Activity implements OnItemClickListener {

	private List<Pool> pools;

	private ListView lvPools;
	private ArrayAdapter<Pool> poolsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "bcmueWmVJzWaQacYZa3bXKHECJMyxzkwUl1plVjz", "0CGF2GuilFib12Jm3rzA3XNwnLtUZ1k8AUzKQNl8");
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Pool pool = pools.get(position);
		Intent intent = new Intent(MainActivity.this, ViewPoolActivity.class);
		startActivity(intent);
	}
}
