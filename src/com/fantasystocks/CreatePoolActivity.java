package com.fantasystocks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fantasystocks.model.Player;
import com.fantasystocks.model.Pool;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreatePoolActivity extends Activity {

	private EditText etPoolName;
	private EditText etPoolFunds;
	private EditText etPlayerLimit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_pool);
		etPoolName = (EditText) findViewById(R.id.etSecuritySymbol);
		etPoolFunds = (EditText) findViewById(R.id.etPoolFunds);
		etPlayerLimit = (EditText) findViewById(R.id.etPlayerLimit);
	}

	public void onSubmitButton(View view) {
		String name = etPoolName.getText().toString();
		double funds = Double.parseDouble(etPoolFunds.getText().toString());
		int playerLimit = Integer.parseInt(etPlayerLimit.getText().toString());

		final Pool pool = new Pool();
		pool.setName(name);
		pool.setFunds(funds);
		pool.setPlayerLimit(playerLimit);
		pool.setPoolImageUrl();
		pool.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException parseException) {
				if (parseException == null) {
					Player player = new Player();
					player.setUser(ParseUser.getCurrentUser());
					player.setPool(pool);
					player.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException parseException) {
							if (parseException != null) {
								parseException.printStackTrace();
							}
							finish();
						}
					});
				} else {
					parseException.printStackTrace();
				}
			}
		});
	}
}
