/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Mu�oz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.manumuve.atryl.R;
import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.data.RssItem;
import com.manumuve.atryl.fragment.FragmentFeedItem;
import com.manumuve.atryl.util.MyConstants;

/** Actividad de consulta de noticia de la aplicaci�n
* Construye la pantalla de consulta de noticia que gestiona
* el un fragment de la clase FragmentFeedItem.
* @author Manumuve
*/
public class FeedItemActivity extends ActionBarActivity { // ActionBarActivity hereda de FragmentActivity
	
	/* Elemento est�tico para poder establecerlo y recuperarlo desde
	 * la activity principal.
	 * Lo correcto es hacerlo utilizando la interfaz Parcelable (serializar el objeto)
	 * http://developer.android.com/reference/android/os/Parcelable.html
	 * pero esta soluci�n es m�s sencilla
	 */
	private static RssItem rssItem = null;
	
	private ShareActionProvider mShareActionProvider;
	
	private static DataSingleton dataSingleton = DataSingleton.getInstance();
	
	FragmentFeedItem fragmentFeedItem;
	
	private Boolean _cleanNowItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//http://developer.android.com/training/basics/activity-lifecycle/recreating.html
		setContentView(R.layout.activity_feed_item);
		//Utils.MyLog('d', "dataSingleton (total: "+dataSingleton.categories.size()+") categoria "+dataSingleton.nowCategory+" feed "+dataSingleton.nowFeed+" item "+dataSingleton.nowItem);
		
		// Forzar comprobar la orientaci�n
		// onConfigurationChanged(getResources().getConfiguration());
		
		_cleanNowItem = false;
		
		// enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        getActionBar().setIcon(R.drawable.ic_action_actionbar_icon);
        
		
		// cargar RssItem (si existe) desde aqu�, el resto de la l�gica se lleva a cabo en
		// el propio fragment
		fragmentFeedItem = (FragmentFeedItem) getSupportFragmentManager().findFragmentById(R.id.fragmentFeedItem);
		
		if (rssItem!=null) {
			fragmentFeedItem.cargarRssItem(rssItem);
			
			// Establecer t�tulo de action bar si existe, o gen�rico si es historia relevante
			if (dataSingleton.nowCategory != MyConstants.NO_DATA && dataSingleton.nowFeed != MyConstants.NO_DATA) {
				getActionBar().setTitle(dataSingleton.categories.get(dataSingleton.nowCategory).getFeed(dataSingleton.nowFeed).getTitle());
				getActionBar().setSubtitle(dataSingleton.categories.get(dataSingleton.nowCategory).getFeed(dataSingleton.nowFeed).getDescription());
			}
			else {
				getActionBar().setTitle(R.string.string_topStories);
				getActionBar().setSubtitle (R.string.string_topStoriesOfNow);
			}
			
			
		}
	}
	
	/**
	 * Construye el men� de la aplicaci�n.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.menu_feed_item_activity, menu);

	    // Set up ShareActionProvider's default share intent
	    final MenuItem shareItem = menu.findItem(R.id.action_shareFeedItem);
	    mShareActionProvider = (ShareActionProvider)
	            MenuItemCompat.getActionProvider(shareItem);
	    mShareActionProvider.setShareIntent(getDefaultIntent());

	    return super.onCreateOptionsMenu(menu);
	}
	
	 /**
	  * Define un share intent por defecto para inicializar el action provider.
	  * Los datos con los que se carga el intent, son los que la
	  * aplicaci�n compartir� con otras aplicaciones.
	  * 
	  * @return el intent con los datos que se comparten.
	  */
	private Intent getDefaultIntent() {
		if (rssItem != null) {
			final Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TITLE, rssItem.getTitle());
			// title y subject se duplica en algunas apps
			//intent.putExtra(Intent.EXTRA_SUBJECT, rssItem.getTitle());

			// Share app's name, if enabled in preferences
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			intent.putExtra(Intent.EXTRA_TEXT,
					"\""
					+ rssItem.getTitle()
					+ ": "
					+ rssItem.getLink()
					+ (sharedPref.getBoolean("preference_IncludeAppName", true) ?
							(" " + getResources().getString(R.string.share_app_reference)) : "")
					+ "\"");

			return intent;
		} else {
			return null;
		}

	}

	/**
	 * M�todo invocado con cada llamada a invalidateOptionsMenu().
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
	/**
	 * M�todo incovado cada vez que se selecciona una opci�n del men�.
	 * 
	 * @return	true si se ha procesado la selecci�n, false si no.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action buttons
		switch(item.getItemId()) {
		
		case R.id.action_viewOnWeb:
			// crear intent para ver noticia en web
			Uri uri = Uri.parse(rssItem.getLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
			}
			return true;
		
		case R.id.action_shareFeedItem:
			Toast.makeText(this, "Compartir", Toast.LENGTH_SHORT).show();
			return true;
		
		// Respond to the action bar's Up/Home button
		// http://developer.android.com/training/implementing-navigation/ancestral.html
	    case android.R.id.home:
	    	Intent upIntent = NavUtils.getParentActivityIntent(this);
	        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
	            // This activity is NOT part of this app's task, so create a new task
	            // when navigating up, with a synthesized back stack.
	            TaskStackBuilder.create(this)
	                    // Add all of this activity's parents to the back stack
	                    .addNextIntentWithParentStack(upIntent)
	                    // Navigate up to the closest parent
	                    .startActivities();
	        } else {
	            // This activity is part of this app's task, so simply
	            // navigate up to the logical parent activity.
	        	_cleanNowItem = true;
	            NavUtils.navigateUpTo(this, upIntent);
	        }
	        
	        // Limpiar datos sobre el item que se estaba viendo
	        _cleanNowItem = true;
	        
	        return true;
	    }
		
		return super.onOptionsItemSelected(item);

	}
	
	/**
	 * M�todo invocado con cada cambio de orientaci�n de la pantalla.
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && dataSingleton._isSmartphone == false) {
	        this.finish();
	    }
	  }
	

	/**
	 * Establece el t�tulo de la barra de acci�n.
	 */
	@Override
	public void setTitle(CharSequence title) {
		//mTitle = title;
		//getActionBar().setTitle(mTitle);
	}

	
	
	
	/** Si se pulsa el bot�n atr�s, limpiar el item mostrado en el fragment
	 * y establecer NODATA en dataSingleton.nowFeed
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		_cleanNowItem = true;
	}
	
	@Override
	protected void onPause() {
		//Utils.MyLog('d', "ONPAUSE");
		super.onPause();
		if (_cleanNowItem == true) {
			dataSingleton.nowItem = MyConstants.NO_DATA;
			fragmentFeedItem.limpiarRssItem();
		}

	}
	
	
	/* M�todos propios de la activity */

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

	/**
	 * Setter para el RssItem que se va a mostrar.
	 * @param item item a mostrar.
	 */
	public static void setRssItem(RssItem item) {
		rssItem = item;
	}

	/**
	 * Getter para el RssItem que se est� mostrando.
	 * @return item que se est� mostrando.
	 */
	public static RssItem getRssItem() {
		return rssItem;
	}
}
