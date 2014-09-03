package com.askaround.mobile.android.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.Bundle;

public abstract class BaseActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

}