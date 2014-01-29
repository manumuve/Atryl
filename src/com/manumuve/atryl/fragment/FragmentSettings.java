package com.manumuve.atryl.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.manumuve.atryl.R;


public class FragmentSettings extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
    
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//Utils.MyLog('d', "FragmentFeedItemList onStart");
		
		// Statistics and tracking, if enabled in preferences
		
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPref.getBoolean("preference_SendAnonStats", true) == true) {
        	EasyTracker tracker = EasyTracker.getInstance(getActivity());
            tracker.set(Fields.SCREEN_NAME, this.getClass().getName());
            tracker.send(MapBuilder.createAppView().build());
        }
	}
}