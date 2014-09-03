package com.askaround.mobile.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.askaround.mobile.android.R;
import com.askaround.mobile.android.app.AskAroundApp;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.askaround.mobile.android.activity.AuthActivity;;
public class LoginActivity extends BaseActivity implements OnClickListener{

	EditText username, password;
	private static final String TAG = "LoginActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);


		username = (EditText) findViewById(R.id.login_username);
		password = (EditText) findViewById(R.id.login_password);
		Button loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(this);
	}


	protected boolean validateFields() {
		boolean valid = true;
		if(username.getText().length() == 0) {
			valid = false;
		}
		if(password.getText().length() == 0) {
			valid = false;
		}
		return valid;
	}

	protected void onLoginSuccess() {
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		finish();
	}


	protected void onLogin() {
		if(validateFields()) {
		ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					onLoginSuccess();
				} else {
					// Signup failed. Look at the ParseException to see what happened.
					if(AskAroundApp.DEBUG) Log.e(TAG, "login failed ->" +  e.getMessage());
				}
			}
		});
		} else {
			Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		int vid = v.getId();

		switch (vid) {
		case R.id.login_button:
			onLogin();
			break;
		}
	}
}
