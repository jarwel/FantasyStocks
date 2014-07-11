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
import com.fantasystocks.util.Utils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class FragmentSignup extends Fragment implements OnClickListener {

	private FragmentActivity listener;
	private EditText etSignUpPassword;
	private EditText etSignUpName;
	private EditText etSignUpEmail;
	private Button btnSignUp;
	private TextView tvSignupError;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.listener = (FragmentActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_signup, container, false);
		setupViews(view);
		return view;
	}

	public void setupViews(View view) {
		etSignUpEmail = (EditText) view.findViewById(R.id.etSignUpEmail);
		etSignUpPassword = (EditText) view.findViewById(R.id.etSignUpPassword);
		etSignUpName = (EditText) view.findViewById(R.id.etSignUpName);
		tvSignupError = (TextView) view.findViewById(R.id.tvSignupError);
		btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
		btnSignUp.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btnSignUp:
			onSignUp();
			break;
		default:
			break;
		}
	}

	public void onSignUp() {
		String name = etSignUpName.getText().toString();
		String password = etSignUpPassword.getText().toString();
		String email = etSignUpEmail.getText().toString();
		if (!name.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
			signup(name, password, email);
		} else {
			tvSignupError.setText("Please check the entered values.");
		}
	}

	/* We are using email = userName */
	public void signup(String name, String password, String email) {
		ParseUser user = new ParseUser();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.put("name", name);
		user.put("imageUrl", Utils.getRandomImageUrl());
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Log.d("Success LoginActivity", "User is signed up");
					((LoginActivity) listener).launchMainActivity();
				} else {
					if (e.getCode() == 125) {
						tvSignupError.setText("Invalid email entered. Please try again.");
					} else if (e.getCode() == 202) {
						tvSignupError.setText("Email already exists. Please try again.");
					} else {
						tvSignupError.setText("Something went wrong. Please try again: " + e.getCode());
					}
				}
			}
		});
	}
}
