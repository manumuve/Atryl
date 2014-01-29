/**
 * http://developer.android.com/guide/components/fragments.html
 */

package com.manumuve.atryl.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.manumuve.atryl.R;
import com.manumuve.atryl.adapter.FeedItemListAdapter;
import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.data.RssItem;
import com.manumuve.atryl.util.MyConstants;
import com.manumuve.atryl.util.Utils;


public class FragmentFeedItemList extends Fragment {

	private DataSingleton dataSingleton=DataSingleton.getInstance();
	private ListView listView;

	
	public FragmentFeedItemList() {
		// Empty constructor required for fragment subclasses
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Utils.MyLog('d', "FragmentFeedItemList onAttach");
        Utils.MyLog('d', "dataSingleton (total: "+dataSingleton.categories.size()+") categoria "+dataSingleton.nowCategory+" feed "+dataSingleton.nowFeed+" item "+dataSingleton.nowItem);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnFeedItemListSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFeedItemListSelectedListener");
        }
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//Utils.MyLog('d', "FragmentFeedItemList onCreate");
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Utils.MyLog('d', "FragmentFeedItemList onCreateView");

		return inflater.inflate(R.layout.fragment_feed_item_list, container, false);
		
	}
	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		//Utils.MyLog('d', "FragmentFeedItemList onActivityCreated");
		
		/* Obtener una instancia a la clase de datos */
		dataSingleton = DataSingleton.getInstance();
		
		listView = (ListView) getView().findViewById(R.id.feedItemList);
		listView.setEmptyView(getView().findViewById(R.id.emptyList));
		
		// si hay datos previos, recuperar estado
		if (dataSingleton._isLoaded) {
			Utils.MyLog('d', "FragmentFeedItem: dataSingleton contiene datos");
			
			
			// No se ha especificado qué feed mostrar
			// Mostrar relevantes
			if (dataSingleton.nowFeed==MyConstants.NO_DATA) {
				listView.setAdapter(new FeedItemListAdapter(dataSingleton.context, dataSingleton.topStories));
			// Mostrar feed indicado
			} else {
				listView.setAdapter(new FeedItemListAdapter(dataSingleton.context, dataSingleton.categories.get(dataSingleton.nowCategory).getFeed(dataSingleton.nowFeed).getItems()));
			}
			
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
					Log.d ("Atryl", "CLIC");

					// establecer item actual
					dataSingleton.nowItem = pos;
					Utils.MyLog('d', "FragmentFeedItemList: "+((RssItem) listView.getAdapter().getItem(pos)).getTitle());
					//mCallback.onFeedItemListSelected(position)
					// ATENCION A ESTA FORMA DE RECUPERAR EL OBJETO QUE SE PULSA
					mCallback.onFeedItemListSelected((RssItem) listView.getAdapter().getItem(pos));

				}
			});
		}
		
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Utils.MyLog('d', "FragmentFeedItemList onResume");
	}
	
	/* FRAGMENT IS ACTIVE */
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//Utils.MyLog('d', "FragmentFeedItemList onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//Utils.MyLog('d', "FragmentFeedItemList onStop");
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		//Utils.MyLog('d', "FragmentFeedItemList onDestroyView");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Utils.MyLog('d', "FragmentFeedItemList onDestroy");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		//Utils.MyLog('d', "FragmentFeedItemList onDetach");
	}

	/* FRAGMENT IS DESTROYED */


    public void mostrarLista(ArrayList<RssItem> list) {
    	
    	listView.setAdapter(new FeedItemListAdapter(dataSingleton.context, list));
       
    }
    
    
    /* COMUNICACION CON ACTIVIDAD CONTENEDORA
     * http://developer.android.com/training/basics/fragments/communicating.html
     */
    OnFeedItemListSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnFeedItemListSelectedListener {
        public void onFeedItemListSelected(RssItem rssItem);
    }
    
}
