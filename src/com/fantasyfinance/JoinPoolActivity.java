package com.fantasyfinance;

import java.util.List;

import com.fantasyfinance.model.Pool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class JoinPoolActivity extends Activity {

	private ListView lvPools;
	private ArrayAdapter<Pool> poolsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_pool);
		lvPools = (ListView) findViewById(R.id.lvPools);
		List<Pool> pools = Pool.getMockPools();
		poolsAdapter = new ArrayAdapter<Pool>(getBaseContext(), android.R.layout.simple_list_item_1, pools);
		lvPools.setAdapter(poolsAdapter);
	}

	public void onCreatePoolClicked(View view) {
		Intent intent = new Intent(JoinPoolActivity.this, CreatePoolActivity.class);
		startActivity(intent);
	}
}
