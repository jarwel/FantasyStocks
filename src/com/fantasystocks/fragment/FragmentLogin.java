package com.fantasystocks.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fantasystocks.LoginActivity;
import com.fantasystocks.R;
import com.fantasystocks.handler.ParseUserHandler;

public class FragmentLogin extends Fragment implements OnClickListener{
	FragmentActivity listener;
	ParseUserHandler handler;
	EditText etLoginPassword;
	EditText etLoginEmail;
	Button btnLogin;
	Button btnSignUpLauncher;
 
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
		handler = new ParseUserHandler(listener);
		return view;
	}
	
	public void setupViews(View view) {
		etLoginEmail = (EditText) view.findViewById(R.id.etLoginEmail);
		etLoginPassword = (EditText) view.findViewById(R.id.etLoginPassword);
		btnLogin = (Button) view.findViewById(R.id.btnLogin);
		btnSignUpLauncher = (Button) view.findViewById(R.id.btnSignUpLauncher);
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
			handler.login(email, password);
		} else {
			Toast.makeText(listener, "Please check the entered values", Toast.LENGTH_SHORT).show();
		}
	}
	
}
