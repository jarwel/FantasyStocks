package com.fantasystocks.handler;

import android.content.Context;
import android.util.Log;

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
	
	public static boolean isUserLoggedIn() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			return false;
		} 
		return true;
	}
	
	/* We are using email = userName */
	public void signup (String name, String password, String email) {
		ParseUser user = new ParseUser();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.add("name", name);
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Log.d("Success LoginActivity", "User is signed up");
					// Handle post sign up flow
				} else {
					Log.d("Error LoginActivity", "" + e);
				}
			}
		});
	}
	
	/* In our case email = username */
	public void login(String email, String password) {
		ParseUser.logInInBackground(email, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					Log.d("Success LoginActivity", "User is logged In with username: " + ParseUser.getCurrentUser().getUsername());
				} else {
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
