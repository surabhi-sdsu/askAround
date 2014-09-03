package com.askaround.mobile.android.activity;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.askaround.mobile.android.R;
import com.askaround.mobile.android.app.AskAroundApp;
import com.parse.LogInCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.ParseQuery;
import com.parse.ParseException;

public class AuthActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "AuthActivity";
	private static final int LOGIN_REQUEST_CODE = 11;
	//private static final int REGISTER_REQUEST_CODE = 12;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);

		Button facebookButton = (Button) findViewById(R.id.auth_fb_button);
		facebookButton.setOnClickListener(this);
		Button loginButton = (Button) findViewById(R.id.auth_login_button);
		loginButton.setOnClickListener(this);
		Button registerButton = (Button) findViewById(R.id.auth_register_button);
		registerButton.setOnClickListener(this);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	protected void onFacebookLogin() {
		//		   LoginActivity.this.progressDialog = ProgressDialog.show(
		//		            LoginActivity.this, "", "Logging in...", true);
		List<String> permissions = Arrays.asList("basic_info", "user_about_me",
				"user_relationships", "user_birthday", "user_location");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				//LoginActivity.this.progressDialog.dismiss();
				if (user == null) {
					if(AskAroundApp.DEBUG) Log.d(TAG,
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					if(AskAroundApp.DEBUG) Log.d(TAG,
							"User signed up and logged in through Facebook!");
					showMainActivity();
				} else {
					if(AskAroundApp.DEBUG)  Log.d(TAG,
							"User logged in through Facebook!");
					showMainActivity();
				}
			}
		});
	}

	protected void showMainActivity() {
		finish();
	}
	protected void onLogin() {
		Intent i = new Intent(this, LoginActivity.class);
		startActivityForResult(i, LOGIN_REQUEST_CODE);
	}

	protected void onRegister() {
		Intent i = new Intent(this, RegisterActivity.class);
		startActivityForResult(i, LOGIN_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
//		ParseFacebookUtils.
//		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);


		if (requestCode == LOGIN_REQUEST_CODE) {

			if(resultCode == RESULT_OK){      
				System.out.println("wtf result ok");
				showMainActivity();
			}
			if (resultCode == RESULT_CANCELED) {    
				//Write your code if there's no result
				System.out.println("wtf result canel");
			}
		}
	}
	
	

	@Override
	public void onClick(View v) {
		int vid = v.getId();

		switch (vid) {
		case R.id.auth_fb_button:
			onFacebookLogin();
			break;
		case R.id.auth_login_button:
			onLogin();
			break;
		case R.id.auth_register_button:
			onRegister();
			break;
		}

	}

}
