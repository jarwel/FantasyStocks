package com.fantasyfinance;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fantasyfinance.model.Pool;

public class CreatePoolActivity extends Activity {

	private EditText etPoolName;
	private EditText etPoolFunds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_pool);
		etPoolName = (EditText) findViewById(R.id.etPoolName);
		etPoolFunds = (EditText) findViewById(R.id.etPoolFunds);
	}

	public void onSubmitButton(View view) {
		String name = etPoolName.getText().toString();
		double funds = Double.parseDouble(etPoolFunds.getText().toString());
		Pool pool = new Pool();
		pool.setName(name);
		pool.setFunds(funds);
		pool.saveInBackground();
		finish();
	}
}
