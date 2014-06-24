package com.fantasystocks.handler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class ParseUserHandler {
	Context context;
	public ParseUserHandler (Context context) {
		this.context = context;
	}
	public void signup (String userName, String password, String email) {
		ParseUser user = new ParseUser();
		user.setUsername(userName);
		user.setPassword(password);
		user.setEmail(email);
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(context, "Success LoginActivity ... User Signed up", Toast.LENGTH_SHORT).show();
					Log.d("Success LoginActivity", "User is signed up");

					// Handle post sign up flow
				} else {
					Toast.makeText(context, "Error LoginActivity .." + e, Toast.LENGTH_SHORT).show();
					Log.d("Error LoginActivity", "" + e);
				}
			}
		});
	}
	
	public void login(String userName, String password) {
		ParseUser.logInInBackground(userName, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					Toast.makeText(context, "Success LoginActivity ... User is logged In with username: " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
					Log.d("Success LoginActivity", "User is logged In with username: " + ParseUser.getCurrentUser().getUsername());
				} else {
					Toast.makeText(context, "Error LoginActivity .." + e, Toast.LENGTH_SHORT).show();
					Log.d("Error LoginActivity", "" + e);
				}
			}
		});
	}
	
	public void getParseUserByEmail (String email) {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("email", email);
		query.getFirstInBackground(new GetCallback<ParseUser>() {
			  public void done(ParseUser user, ParseException e) {
			    if (user == null) {
			      // Nothing found
			    } else {
			      // found user Object
			    }
			  }
			});
	}
}
