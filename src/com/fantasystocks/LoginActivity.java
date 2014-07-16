package com.fantasystocks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.fantasystocks.fragment.FragmentLogin;
import com.fantasystocks.fragment.FragmentSignup;

public class LoginActivity extends FragmentActivity {

	private Fragment loginFragment;
	private Fragment signupFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (loginFragment == null) {
			loginFragment = new FragmentLogin();
		}
		doFragmentTransaction(loginFragment, false);
	}

	public void doFragmentTransaction(Fragment fragment, boolean addToBackStack) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.frame_login, fragment);
		if (addToBackStack) {
			ft.addToBackStack(null);
		}
		ft.commit();
	}

	/* Listening to events in fragment */
	public void launchMainActivity() {
		Intent i = new Intent(this, HomeActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}
	
	/* Listening to events in fragment */
	public void launchTourActivity() {
		Intent i = new Intent(this, TourActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}

	/* Listening to events in fragment */
	public void onSignUpLauncherClick() {
		if (signupFragment == null) {
			signupFragment = new FragmentSignup();
		}
		doFragmentTransaction(signupFragment, true);
	}
}
