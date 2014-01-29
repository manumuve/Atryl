package com.manumuve.atryl.fragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.manumuve.atryl.R;
import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.data.RssItem;
import com.manumuve.atryl.util.MyConstants;
import com.manumuve.atryl.util.Utils;





public class FragmentFeedItem extends Fragment {

	private WebView webView;
	
	private DataSingleton dataSingleton=DataSingleton.getInstance();
	
	public static RssItem rssItem;
	
	private String styleSheet;


	public FragmentFeedItem() {
		// Empty constructor required for fragment subclasses

	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//Utils.MyLog('d', "FragmentFeedItem onAttach");

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//Utils.MyLog('d', "FragmentFeedItem onCreate");
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//Utils.MyLog('d', "FragmentFeedItem onCreateView");
		
		return inflater.inflate(R.layout.fragment_feed_item, container, false);

	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		//Utils.MyLog('d', "FragmentFeedItem onActivityCreated");

		webView = (WebView) getView().findViewById(R.id.webView);
		
		/* Si hay cargado un RssItem, mostrarlo */
		if (rssItem != null) {
			Utils.MyLog('d', "Existe un RssItem previo, lo muestro");
			mostrarRssItem ();
		}
		/* Si no, verificar que existe item indicado en el Singleton y cargarlo */
		else if (dataSingleton.nowItem != MyConstants.NO_DATA){
			//TODO: mejorar teniendo en cuenta categoría destacadas
			dataSingleton = DataSingleton.getInstance();
			if (dataSingleton._isLoaded) {
				Utils.MyLog('d', "dataSingleton contiene datos");
				if (dataSingleton.nowItem != MyConstants.NO_DATA) {
					Utils.MyLog('d', "dataSingleton me pide mostrar el item "+dataSingleton.nowCategory+" "+dataSingleton.nowFeed+" "+dataSingleton.nowItem);
					rssItem = dataSingleton.categories
							  .get(dataSingleton.nowCategory)
							  .getFeed(dataSingleton.nowFeed).getItem(dataSingleton.nowItem);
					
					mostrarRssItem ();
				}
			}
		}

	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//Utils.MyLog('d', "FragmentFeedItem onStart");
		
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
		//Utils.MyLog('d', "FragmentFeedItem onResume");
	}
	
	/* FRAGMENT IS ACTIVE */
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//Utils.MyLog('d', "FragmentFeedItem onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//Utils.MyLog('d', "FragmentFeedItem onStop");
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		//Utils.MyLog('d', "FragmentFeedItem onDestroyView");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Utils.MyLog('d', "FragmentFeedItem onDestroy");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		//Utils.MyLog('d', "FragmentFeedItem onDetach");
	}

	/* FRAGMENT IS DESTROYED */
	
	/* Métodos propios del fragment */
	
	public void cargarRssItem(RssItem item) {
		Utils.MyLog('d', "Cargando item");
		rssItem = item;
		
	}
	
	public Boolean mostrarRssItem () {
		if (rssItem!=null) {
			Utils.MyLog('d', "Mostrando item");
			
			//TODO: formatear contenido, sería parte de este codigo de abajo
			
			String fullItem;
			String content;
			
			fullItem = "";
			
			if (dataSingleton._isSmartphone == true) {
				styleSheet = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/smartphone.css\" />";
			} else {
				styleSheet = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/tablet.css\" />";
			}
			
			fullItem = fullItem + styleSheet;
			
			fullItem = fullItem + "<div class=\"atryl-title\">" + rssItem.getTitle() + "</div>";
			
			if (rssItem.getAuthor() != null) {
				fullItem = fullItem + "<div class=\"atryl-author\">" + rssItem.getAuthor() + "</div>";
			}
			
			if (rssItem.getPubDate() != null) {
				String pubDate = new SimpleDateFormat(MyConstants.DATE_FORMAT,	Locale.getDefault()).format(rssItem.getDatePubDate());
				fullItem = fullItem + "<div class=\"atryl-pubdate\">" + pubDate + "</div>";
			}
			
			/* Remove inner style tags */
			content = rssItem.getContentEncoded();
			content = content.replaceAll(" style[\\d\\D]*?;\"", "");
			Log.d ("ATRYL", content);
			
			fullItem = fullItem + content;

			/* load fullItem in webview */
			webView.loadDataWithBaseURL("file:///android_asset/", fullItem, "text/html", "UTF-8", null);

			return true;
		}
		
		return false;
	}
	
	public void limpiarRssItem () {
		rssItem = null;
	}
	
	public void emptyItem () {
		// Método recomendado en:
		// http://developer.android.com/reference/android/webkit/WebView.html#clearView()
		webView.loadUrl("about:blank");
		limpiarRssItem();
	}
	
}