package com.manumuve.atryl.activity;

import android.app.Activity;
import android.os.Bundle;

import com.manumuve.atryl.fragment.FragmentSettings;

public class SettingsActivity extends Activity {
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new FragmentSettings())
                .commit();
    }

}
