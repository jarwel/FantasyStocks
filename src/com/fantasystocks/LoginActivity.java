package com.fantasystocks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.EditText;

import com.fantasystocks.fragment.FragmentLogin;
import com.fantasystocks.fragment.FragmentSignup;
import com.fantasystocks.handler.ParseUserHandler;
import com.parse.ParseUser;

public class LoginActivity extends FragmentActivity {
	Fragment loginFragment;
	Fragment signupFragment;
	ParseUserHandler handler;
	EditText etLoginPassword;
	EditText etLoginEmail;
	EditText etSignUpName;
	EditText etSignUpPassword;
	EditText etSignUpEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (ParseUserHandler.isUserLoggedIn()) {
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
		} else {
			setContentView(R.layout.activity_login); 
			if (loginFragment == null) {
				loginFragment = new FragmentLogin();
			}
			doFragmentTransaction(loginFragment, true);
			handler = new ParseUserHandler(this);
		}
	}
	
	public void onSignUpLauncherClick() {
		if (signupFragment == null) {
			signupFragment = new FragmentSignup();
		}
		doFragmentTransaction(signupFragment, true);
	}
	
	public void doFragmentTransaction (Fragment fragment, boolean addToBackStack) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.frame_login, fragment);
		if (addToBackStack) {
			ft.addToBackStack(null);
		}
		ft.commit();
	}
}
