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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends BaseActivity implements OnClickListener{

	private EditText email, username, password;
	private static final String TAG = "RegisterActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		 email = (EditText) findViewById(R.id.register_email);
		 username = (EditText) findViewById(R.id.register_username);
		 password = (EditText) findViewById(R.id.register_password);
		 Button registerButton = (Button) findViewById(R.id.register_button);
		 registerButton.setOnClickListener(this);
	}

	protected boolean validateFields() {
		boolean valid = true;
		if(username.getText().length() == 0) {
			valid = false;
		}
		if(password.getText().length() == 0) {
			valid = false;
		}
		if(email.getText().length() == 0) {
			valid = false;
		}
		return valid;
	}
	
	protected void onRegistrationSuccess() {
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		finish();
	}
	
	protected void onRegister() {
		if(validateFields()) {
			ParseUser user = new ParseUser();
			user.setUsername(username.getText().toString());
			user.setPassword(password.getText().toString());
			user.setEmail(email.getText().toString());

			// other fields can be set just like with ParseObject
			//user.put("phone", "650-253-0000");

			user.signUpInBackground(new SignUpCallback() {
				public void done(ParseException e) {
					if (e == null) {
						onRegistrationSuccess();
					} else {
						// Sign up didn't succeed. Look at the ParseException
						// to figure out what went wrong
						if(AskAroundApp.DEBUG) Log.e(TAG, "wtf register falied -> " + e.getMessage());
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
		case R.id.register_button:
			onRegister();
			break;
		}
	}
}
