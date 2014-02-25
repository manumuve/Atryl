/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
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

/**
 * Clase encargada de gestionar la porción de pantalla donde se muestra
 * la pantalla de carga inicial de la aplicación.
 * @author Manu
 *
 */
public class FragmentLoadingScreen extends Fragment {
	
	private DataSingleton dataSingleton;
	
	private TextView txLoading;
	
	private ProgressBar progressBar;
	
	/**
	 * Constructor estándar.
	 */
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

	/**
	 * Devuelve la vista que se va a utilizar. 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Utils.MyLog('d', "FragmentFeedItemList onCreateView");

		return inflater.inflate(R.layout.fragment_loading_screen, container, false);
		
	}
	
	/**
	 * Enlaza los componentes de la vista con variables del método,
	 * resetea las variables de estado de ejecución del singleton
	 * y configura la barra de progreso para trabajar con porcentajes.
	 */
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
		
		/* Aviso si no hay conexión */
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
	
	/**
	 * Actualiza la barra de progreso con el progreso actual.
	 * @param progress	porcentaje cargado.
	 * @param feedTitle	título del feed cargado.
	 */
	public void showProgress (int progress, String feedTitle) {
		progressBar.setProgress(progress);
		txLoading.setText(feedTitle + "...");
	}
	/* --------------------------------------------------------------------- */
}
