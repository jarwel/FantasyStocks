package com.fantasystocks;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fantasystocks.handler.ParseUserHandler;
import com.parse.ParseUser;

public class LoginActivity extends Activity {
	ParseUserHandler handler;
	EditText etUserName;
	EditText etPassword;
	EditText etEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ParseUser currentUser = ParseUser.getCurrentUser();
		setContentView(R.layout.activity_login);
		setupViews();
		if (currentUser == null) {
			//setContentView(R.layout.activity_login);
			handler = new ParseUserHandler(this);
		} else {
			Log.d("Success LoginActivity", "User was already logged In with username: " + ParseUser.getCurrentUser().getUsername());
		}
	}
	
	public void setupViews() {
		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etEmail = (EditText) findViewById(R.id.etEmail);
	}

	public void signup(View view) {
		String userName = etUserName.getText().toString();
		String password = etPassword.getText().toString();
		String email = etEmail.getText().toString();
		if (!userName.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
			handler.signup(userName, password, email);
		} else {
			Toast.makeText(this, "Please check entered values", Toast.LENGTH_LONG).show();
		}
	}

	public void login(View view) {
		String userName = etUserName.getText().toString();
		String password = etPassword.getText().toString();
		if (!userName.isEmpty() && !password.isEmpty()) {
			handler.login(userName, password);
		} else {
			Toast.makeText(this, "Please check entered values", Toast.LENGTH_LONG).show();
		}
	}
	
	public void logout(View view) {
		ParseUser.logOut();
	}
}
