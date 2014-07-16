package com.fantasystocks;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TourActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour);
	}
	
	public void launchCreatePool (View v) {
		Intent createIntent = new Intent(this, CreatePoolActivity.class);
		startActivity(createIntent);
	}
	
	public void launchFindPool (View v) {
		Intent createIntent = new Intent(this, FindPoolActivity.class);
		startActivity(createIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_tour, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_logout:
			ParseUser.logOut();
			Intent i = new Intent(this, LoginActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
