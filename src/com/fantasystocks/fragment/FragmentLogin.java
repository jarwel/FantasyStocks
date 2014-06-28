package com.fantasystocks.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fantasystocks.LoginActivity;
import com.fantasystocks.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class FragmentLogin extends Fragment implements OnClickListener{
	FragmentActivity listener;
	EditText etLoginPassword;
	EditText etLoginEmail;
	Button btnLogin;
	Button btnSignUpLauncher;
	TextView tvLoginError;
 
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.listener = (FragmentActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		setupViews(view);
		return view;
	}
	
	public void setupViews(View view) {
		etLoginEmail = (EditText) view.findViewById(R.id.etLoginEmail);
		etLoginPassword = (EditText) view.findViewById(R.id.etLoginPassword);
		btnLogin = (Button) view.findViewById(R.id.btnLogin);
		btnSignUpLauncher = (Button) view.findViewById(R.id.btnSignUpLauncher);
		tvLoginError = (TextView) view.findViewById(R.id.tvLoginError);
		btnLogin.setOnClickListener(this);
		btnSignUpLauncher.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch(id) {
		case R.id.btnLogin:
			onLoginClick();
			break;
		case R.id.btnSignUpLauncher:
			((LoginActivity) listener).onSignUpLauncherClick();
			break;		
		default:
			break;	
		}
	}
	
	public void onLoginClick() {
		String email = etLoginEmail.getText().toString();
		String password = etLoginPassword.getText().toString();
		if (!email.isEmpty() && !password.isEmpty()) {
			login(email, password);
		} else {
			tvLoginError.setText("Please check the entered values.");
		}
	}
	
	/* In our case email = username */
	public void login(String email, String password) {
		ParseUser.logInInBackground(email, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					Log.d("Success LoginActivity", "User is logged In with username: " + ParseUser.getCurrentUser().getUsername());
					((LoginActivity) listener).launchMainActivity();
				} else {
					if (e.getCode() == 101) {
						tvLoginError.setText("The credentials you entered are incorrect.");
					} else {
						tvLoginError.setText("Something went wrong. Please try again: " + e.getCode());
					}
					
				}
			}
		});
	}
	
}
