package com.fantasystocks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;

import com.fantasystocks.fragment.FragmentLogin;
import com.fantasystocks.fragment.FragmentSignup;
import com.fantasystocks.utils.Utils;

public class LoginActivity extends FragmentActivity {
	Fragment loginFragment;
	Fragment signupFragment;
	EditText etLoginPassword;
	EditText etLoginEmail;
	EditText etSignUpName;
	EditText etSignUpPassword;
	EditText etSignUpEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (Utils.isUserLoggedIn()) {
			launchMainActivity();
		} else {
			setContentView(R.layout.activity_login); 
			if (loginFragment == null) {
				loginFragment = new FragmentLogin();
			}
			doFragmentTransaction(loginFragment, false);
		}
	}
	
	public void launchMainActivity() {
		Intent i = new Intent(this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
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
