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

public class FeedItemActivity extends ActionBarActivity { // ActionBarActivity hereda de FragmentActivity
	
	/* Elemento estático para poder establecerlo y recuperarlo desde
	 * la activity principal.
	 * Lo correcto es hacerlo utilizando la interfaz Parcelable (serializar el objeto)
	 * http://developer.android.com/reference/android/os/Parcelable.html
	 * pero esta solución es más sencilla
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
		
		// Forzar comprobar la orientación
		// onConfigurationChanged(getResources().getConfiguration());
		
		_cleanNowItem = false;
		
		// enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        getActionBar().setIcon(R.drawable.ic_action_actionbar_icon);
        
		
		// cargar RssItem (si existe) desde aquí, el resto de la lógica se lleva a cabo en
		// el propio fragment
		fragmentFeedItem = (FragmentFeedItem) getSupportFragmentManager().findFragmentById(R.id.fragmentFeedItem);
		
		if (rssItem!=null) {
			fragmentFeedItem.cargarRssItem(rssItem);
			
			// Establecer título de action bar si existe, o genérico si es historia relevante
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
	   * Defines a default (dummy) share intent to initialize the action provider.
	   * However, as soon as the actual content to be used in the intent
	   * is known or changes, you must update the share intent by again calling
	   * mShareActionProvider.setShareIntent()
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
					  rssItem.getTitle()
					  + ": "
					  + rssItem.getLink()
					  + (sharedPref.getBoolean("preference_IncludeAppName", true) ?
							  (" " + getResources().getString(R.string.share_app_reference)) : "")
					  );

			  return intent;
		  } else {
			  return null;
		  }
		  
	  }

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
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
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && dataSingleton._isSmartphone == false) {
	        this.finish();
	    }
	  }
	

	@Override
	public void setTitle(CharSequence title) {
//		mTitle = title;
//		getActionBar().setTitle(mTitle);
	}

	
	
	
	/** Si se pulsa el botón atrás, limpiar el item mostrado en el fragment
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
	
	
	/* Métodos propios de la activity */

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

	public static void setRssItem(RssItem item) {
		rssItem = item;
	}

	public static RssItem getRssItem() {
		return rssItem;
	}
}
