package com.manumuve.atryl.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.analytics.tracking.android.EasyTracker;
import com.manumuve.atryl.R;

public class HelpActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//http://developer.android.com/training/basics/activity-lifecycle/recreating.html
		setContentView(R.layout.activity_help);
		
		// enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		
        getActionBar().setIcon(R.drawable.ic_action_actionbar_icon);
		
	}
	


	@Override
	protected void onStart() {
		super.onStart();
		
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	}

	@Override
	protected void onStop() {
		super.onStop();
		
        EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	}

}
