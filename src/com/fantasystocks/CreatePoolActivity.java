package com.fantasystocks;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

import com.fantasystocks.model.Pool;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreatePoolActivity extends Activity implements OnClickListener {

	private EditText etPoolName;
	private EditText etPoolFunds;
	private EditText etPlayerLimit;
	private EditText etStartDate;
	private EditText etEndDate;

	private Date startDate;
	private Date endDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_pool);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		etPoolName = (EditText) findViewById(R.id.etPoolName);
		etPoolFunds = (EditText) findViewById(R.id.etPoolFunds);
		etPlayerLimit = (EditText) findViewById(R.id.etPlayerLimit);
		etStartDate = (EditText) findViewById(R.id.etStartDate);
		etEndDate = (EditText) findViewById(R.id.etEndDate);
		etStartDate.setOnClickListener(this);
		etEndDate.setOnClickListener(this);
	}

	public void onSubmitButton(View view) {
		String name = etPoolName.getText().toString();
		double funds = Double.parseDouble(etPoolFunds.getText().toString());
		int playerLimit = Integer.parseInt(etPlayerLimit.getText().toString());

		final Pool pool = new Pool();
		pool.setName(name);
		pool.setFunds(funds);
		pool.setPlayerLimit(playerLimit);
		pool.setStartDate(startDate);
		pool.setEndDate(endDate);
		pool.setPoolImageUrl();
		pool.setCanonicalName(name);
		pool.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException parseException) {
				if (parseException == null) {
					pool.addPortfolio(ParseUser.getCurrentUser(), new SaveCallback() {
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(final View view) {
		new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
				switch (view.getId()) {
				case R.id.etStartDate:
					startDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
					etStartDate.setText(RestApplication.getFormatter().formatFullDate(startDate));
					break;
				case R.id.etEndDate:
					endDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
					etEndDate.setText(RestApplication.getFormatter().formatFullDate(endDate));
					break;
				}
			}
		}, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
	}

}
