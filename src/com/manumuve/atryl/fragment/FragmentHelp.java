package com.manumuve.atryl.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.manumuve.atryl.R;

public class FragmentHelp  extends Fragment {
	
	public FragmentHelp() {
		// Empty constructor required for fragment subclasses

	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_help, container, false);

	}
	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);

		//TextView = (TextView) getView().findViewById(R.id.);

	}
    
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		// Statistics and tracking, if enabled in preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPref.getBoolean("preference_SendAnonStats", true) == true) {
        	EasyTracker tracker = EasyTracker.getInstance(getActivity());
            tracker.set(Fields.SCREEN_NAME, this.getClass().getName());
            tracker.send(MapBuilder.createAppView().build());
        }
	}
	
	 /**
	   * Defines a default (dummy) share intent to initialize the action provider.
	   * However, as soon as the actual content to be used in the intent
	   * is known or changes, you must update the share intent by again calling
	   * mShareActionProvider.setShareIntent()
	   */
	  private Intent getDefaultIntent() {
		 
			  final Intent intent = new Intent(Intent.ACTION_SEND);
			  intent.setType("text/plain");
			  intent.putExtra(Intent.EXTRA_TITLE, "titulo");
			  intent.putExtra(Intent.EXTRA_SUBJECT, "sujeto");
			  
			  return intent;
		  
	  }
}
