/**
 * TODO: La carga de feeds se hace secuencialmente
 * tarda mucho, hay que cambiarlo a un modelo de carga
 * simultanea: lanzar varias tareas asíncronas a la vez.
 * NOTA: AsyncTask por defecto actua secuencialmente. Dejar así de momento.
 * http://manishkpr.webheavens.com/android-navigation-drawer-example-using-fragments/
 */

package com.manumuve.atryl.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.manumuve.atryl.R;
import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.util.MyConstants;
import com.manumuve.atryl.util.Utils;


public class FragmentLoadingScreen extends Fragment {
	
	private DataSingleton dataSingleton;
	
	private TextView txLoading;
	
	private ProgressBar progressBar;
	
	public FragmentLoadingScreen() {
		// Empty constructor required for fragment subclasses
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Utils.MyLog('d', "FragmentLoadingScreen onAttach");
        
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//Utils.MyLog('d', "FragmentFeedItemList onCreate");
		
		// Retain this instance so it isn't destroyed when MainActivity and
        // MainFragment change configuration.
        setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Utils.MyLog('d', "FragmentFeedItemList onCreateView");

		return inflater.inflate(R.layout.fragment_loading_screen, container, false);
		
	}
	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		//Utils.MyLog('d', "FragmentFeedItemList onActivityCreated");
		
		dataSingleton = DataSingleton.getInstance();
		
		// No hay datos establecidos que mostrar
		dataSingleton.nowCategory = MyConstants.NO_DATA;
		dataSingleton.nowFeed = MyConstants.NO_DATA;
		dataSingleton.nowItem = MyConstants.NO_DATA;
		
		txLoading = (TextView) getView().findViewById(R.id.txLoading);
		
		progressBar = (ProgressBar) getView().findViewById(R.id.progressBar1);
		progressBar.setMax(100); // trabajar con porcentajes
		
		/* Aviso si no hay conexión */ //TODO: pasar a main activity
		if (!Utils.checkConnectivity()) {
			Utils.MyLog('d', "No existe conexión a Internet");
			Toast.makeText(dataSingleton.context, "No existe conexión a Internet", Toast.LENGTH_LONG).show();
		}
		
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//Utils.MyLog('d', "FragmentLoadingScreen onStart");
		
		// Statistics and tracking, if enabled in preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPref.getBoolean("preference_SendAnonStats", true) == true) {
        	EasyTracker tracker = EasyTracker.getInstance(getActivity());
            tracker.set(Fields.SCREEN_NAME, this.getClass().getName());
            tracker.send(MapBuilder.createAppView().build());
        }
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Utils.MyLog('d', "FragmentLoadingScreen onResume");
	}
	
	/* FRAGMENT IS ACTIVE */
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//Utils.MyLog('d', "FragmentLoadingScreen onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//Utils.MyLog('d', "FragmentLoadingScreen onStop");
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		//Utils.MyLog('d', "FragmentLoadingScreen onDestroyView");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Utils.MyLog('d', "FragmentLoadingScreen onDestroy");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		//Utils.MyLog('d', "FragmentLoadingScreen onDetach");
	}

	/* FRAGMENT IS DESTROYED */
	
	
	
	
	/* Métodos propios ----------------------------------------------------- */
	public void showProgress (int progress, String feedTitle) {
		progressBar.setProgress(progress);
		txLoading.setText(feedTitle + "...");
	}
	/* --------------------------------------------------------------------- */
}
