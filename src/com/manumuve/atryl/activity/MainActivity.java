/*	Atryl: smart feed reader for Android devices
    Copyright (C) 2013 @manumuve
    
    This file is part of Atryl.
*/

package com.manumuve.atryl.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.manumuve.atryl.R;
import com.manumuve.atryl.adapter.NavigationDrawerAdapter;
import com.manumuve.atryl.asynctask.XmlLoad;
import com.manumuve.atryl.asynctask.XmlLoadInterface;
import com.manumuve.atryl.data.AlmacenStructInterface;
import com.manumuve.atryl.data.AlmacenStructXML_SAX;
import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.data.RssCategory;
import com.manumuve.atryl.data.RssFeed;
import com.manumuve.atryl.data.RssItem;
import com.manumuve.atryl.dialog.DialogAddFeed;
import com.manumuve.atryl.dialog.DialogEditCategory;
import com.manumuve.atryl.dialog.DialogEditFeed;
import com.manumuve.atryl.dialog.MyDialogInterface;
import com.manumuve.atryl.fragment.FragmentFeedItem;
import com.manumuve.atryl.fragment.FragmentFeedItemList;
import com.manumuve.atryl.fragment.FragmentHelp;
import com.manumuve.atryl.fragment.FragmentLoadingScreen;
import com.manumuve.atryl.util.MyConstants;
import com.manumuve.atryl.util.RelevantHistoryMaker;
import com.manumuve.atryl.util.Utils;


/** Actividad principal de la aplicaci�n
* Construye la interfaz de usuario y se encarga de gestionarla.
* @author Manumuve
* @version 0.1
*/
public class MainActivity extends ActionBarActivity implements XmlLoadInterface, FragmentFeedItemList.OnFeedItemListSelectedListener, MyDialogInterface {
	
	
	/** Variables */
	/* Actionbar asociada a Navigation Drawer */
	private ActionBarDrawerToggle mDrawerToggle;
	
	/* Lista de suscripciones */
	private ExpandableListView mDrawerList; 
	
	/* Layout que contiene la Navigation Drawer */
	private DrawerLayout mDrawerLayout;
	
	private FrameLayout mDrawer;
	
	/* T�tulo de la Navigation Drawer */
	private CharSequence mDrawerTitle; 
	
	/* T�tulo de la aplicaci�n */
	private CharSequence mTitle;
	
	/* Proveedor de acciones compartir */
	private ShareActionProvider mShareActionProvider;
	
	/* Manejador de fragments */
	private FragmentManager fragmentManager;
	
	/* Transici�n entre fragments */
	private FragmentTransaction fragmentTransaction;
	
	/* Fragments de la activity */
	private Fragment fragmentLoadingScreen, fragmentFeedItem, fragmentFeedItemList, fragmentHelp;
	
	/* Almacen de estructura y datos. Vol�til. Patr�n Singleton */
	private static DataSingleton dataSingleton;
	
	/* Almacen de estructura. Persistente */
	public static AlmacenStructInterface almacen;
	
	/* Clase constructora del listado de noticias destacadas */
	private RelevantHistoryMaker relevantHistoryMaker = new RelevantHistoryMaker();
	
	/* Bot�n noticias destacadas */
	private Button btTopStories;
	
	/* Variables de men� */
	private MenuItem menuItemShareItem;
	private MenuItem menuItemAddFeed;
	private MenuItem menuItemSettings;
	private MenuItem menuItemHelp; 
	private MenuItem menuItemRefresh;
	private MenuItem menuItemViewOnWeb;
	
	
	
	
	/** onCreate
	 * Called when the activity is starting. This is where most initialization
	 * should go: calling setContentView(int) to inflate the activity's UI,
	 * using findViewById(int) to programmatically interact with widgets in the
	 * UI, calling managedQuery(android.net.Uri, String[], String, String[],
	 * String) to retrieve cursors for data being displayed, etc.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Utils.MyLog('d', "MainActivity onCreate");
		setContentView(R.layout.activity_main);
		
		/* Establecer el m�todo almacen utilizado */
		if (metodoAlmacen(MyConstants.SAX_PARSER) == MyConstants.ERROR) {
			Utils.MyLog ('d', "No se pudo establecer un m�todo de almacen");
			finish();
		}
		
		/* Obtener instancia de la clase de datos */
		dataSingleton = DataSingleton.getInstance();
		if (dataSingleton == null) {
			Utils.MyLog ('d', "No se pudo instanciar la clase de datos");
			finish();
		}
		
		/* Establecer contexto en singleton */
		dataSingleton.context = this;
		
		/* Establecer si es tablet o smartphone */
		dataSingleton._isSmartphone = Utils.isSmartphone(this);
		
		/* Fragments de la activity */
		// Obtener fragment manager
		fragmentManager = getSupportFragmentManager();

		fragmentLoadingScreen = fragmentManager.findFragmentByTag("fragmentLoadingScreen");
        if (fragmentLoadingScreen == null) fragmentLoadingScreen = new FragmentLoadingScreen();
       
        fragmentFeedItem = fragmentManager.findFragmentByTag("fragmentFeedItem");
        if (fragmentFeedItem == null) fragmentFeedItem = new FragmentFeedItem();
      
        fragmentFeedItemList = fragmentManager.findFragmentByTag("fragmentFeedItemList");
		if (fragmentFeedItemList == null) fragmentFeedItemList = new FragmentFeedItemList();
		
		fragmentHelp = fragmentManager.findFragmentByTag("fragmentHelp");
		if (fragmentHelp == null) fragmentHelp = new FragmentHelp();		
		
		
		/* Establecer variables de t�tulo */
		mTitle = mDrawerTitle = getTitle();

		// enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // cambiar icono
        getActionBar().setIcon(R.drawable.ic_action_actionbar_icon);
		
		/* Bot�n noticias relevantes */
		btTopStories = (Button) findViewById(R.id.btRelevant);
		btTopStories.setOnClickListener(new BtRelevantOnClickListener());
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ExpandableListView) findViewById(R.id.ExpandableListNavigationDrawer);
        mDrawer = (FrameLayout) findViewById (R.id.navigationDrawer); // necesario para cerrar drawer

		// set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new NavigationDrawerAdapter(dataSingleton.categories));
        mDrawerList.setOnChildClickListener(new DrawerItemClickListener());
        mDrawerList.setOnGroupClickListener(new DrawerItemClickListener());
        mDrawerList.setOnItemLongClickListener(new DrawerItemClickListener());
        
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            @Override
			public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
			public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        /* Si no hay datos */
		if (dataSingleton._isLoaded == false) {
			// obtener la estructura de categor�as y feeds
			Utils.MyLog ('d', "Cargando estructura de suscripciones");
			dataSingleton.categories = almacen.getFeedsStruct();
			
			// disable ActionBar app icon to behave as action to toggle nav drawer
			getActionBar().setHomeButtonEnabled(false); // true en XmlLoadInterface
		}
		
		/* Si no hay categor�as �Bienvenido! */
		if (dataSingleton.categories.size() == 0) { // no hay datos
			// TODO: string
			Toast.makeText(this, "�Bienvenido a Atryl!", Toast.LENGTH_LONG).show();
			
			// ocultar bot�n relevantes
			btTopStories.setVisibility(View.GONE);
			
			// Si fragmentFeedItemList est� a�adido, borrarlo
			if (fragmentFeedItemList.isAdded()) {
				Utils.MyLog ('d', "remove fragmentFeedItemList");
				getSupportFragmentManager()
				.beginTransaction()
				.remove(fragmentFeedItemList)
				.commit();
			}
			
			// Add the fragmentHelp to the 'fragment_container' FrameLayout
			if (fragmentHelp.isAdded() == false) {
				Utils.MyLog ('d', "add fragmentHelp");
				getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragmentContainer, fragmentHelp, "fragmentHelp")
				.commit();
			}
		// Si S� hay categor�as (m�nimo existe un feed)	
		} else {

			/* Carga del fragment loading o fragment feed item list, seg�n convenga.
			 * Referencia en:
			 * http://developer.android.com/training/basics/fragments/fragment-ui.html
			 */
			// Si NO hay datos cargados en el singleton
			if (dataSingleton._isLoaded == false) {

				// Mantener cerrado el panel lateral
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

				// Crear una instancia de FragmentLoadingScreen
				//fragmentLoadingScreen = new FragmentLoadingScreen();

				// In case this activity was started with special instructions from an Intent,
				// pass the Intent's extras to the fragment as arguments
				fragmentLoadingScreen.setArguments(getIntent().getExtras());

				// Add the fragment to the 'fragment_container' FrameLayout
				getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragmentContainer, fragmentLoadingScreen, "fragmentLoadingScreen")
				.commit();

				/* Cargar feeds */
				refreshFeeds ();

			}
			
			// Si HAY datos cargados en el singleton
			else {
				
				// Si fragmentHelp est� a�adido, borrarlo
				if (fragmentHelp.isAdded()) {
					Utils.MyLog ('d', "remove fragmentHelp");
					getSupportFragmentManager()
					.beginTransaction()
					.remove(fragmentHelp)
					.commit();
				}
				
				// Si no est� a�adido, a�adirlo
				if (fragmentFeedItemList.isAdded() == false) {
					Utils.MyLog ('d', "add fragmentFeedItemList");
					getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.fragmentContainer, fragmentFeedItemList, "fragmentFeedItemList")
					.commit();
				}
				
				
			}
		}
	}

	@Override
    protected void onStart() {
        super.onStart();
        //Utils.MyLog('v', "MainActivity onStart");
        // The activity is about to become visible.
        
        // Statistics and tracking, if enabled in preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.getBoolean("preference_SendAnonStats", true) == true) {
        	EasyTracker.getInstance(this).activityStart(this);  // Add this method.
        }
        
        
        
        /* Comprobar cambios de orientaci�n en la tableta */
		if (dataSingleton._isSmartphone == false) {
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				if (dataSingleton.nowItem != MyConstants.NO_DATA) {
					Utils.MyLog('d', "Rotando a retrato -> Mostrar item");
					if (dataSingleton.nowCategory == MyConstants.TOP_STORIES) {
						mostrarItem (dataSingleton.topStories.get(dataSingleton.nowItem));
					}
					else {
						mostrarItem (dataSingleton.categories
								.get(dataSingleton.nowCategory)
								.getFeed(dataSingleton.nowFeed)
								.getItem(dataSingleton.nowItem));
					}
				}
			}
		}
		
    }
    @Override
    protected void onResume() {
        super.onResume();
        //Utils.MyLog('v', "MainActivity onResume");
        // The activity has become visible (it is now "resumed").

        
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Utils.MyLog('v', "MainActivity onPause");
        // Another activity is taking focus (this activity is about to be "paused").
        
        /* Guardar orientaci�n de pantalla */
        dataSingleton.previousOrientation = getResources().getConfiguration().orientation;
    }
    @Override
    protected void onStop() {
        super.onStop();
        //Utils.MyLog('v', "MainActivity onStop");
        // The activity is no longer visible (it is now "stopped")
        
        // Statistics and tracking, if enabled in preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.getBoolean("preference_SendAnonStats", true) == true) {
        	EasyTracker.getInstance(this).activityStop(this);  // Add this method.
        }
        
        
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Utils.MyLog('v', "MainActivity onDestroy");
        // The activity is about to be destroyed.
    }
    
    
    
    
	/* Menu ---------------------------------------------------------------- */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		
		inflater.inflate(R.menu.menu_main_activity, menu);
		
		fragmentFeedItem = fragmentManager.findFragmentByTag("fragmentFeedItem");
		if (fragmentFeedItem != null) {
			// Modo tablet horizontal
			if (fragmentFeedItem.isAdded()) {

			    // Set up ShareActionProvider's default share intent
			    final MenuItem shareItem = menu.findItem(R.id.action_shareFeedItem);
			    mShareActionProvider = (ShareActionProvider)
			            MenuItemCompat.getActionProvider(shareItem);
			    mShareActionProvider.setShareIntent(getShareIntent());
			}
		}
		
		return super.onCreateOptionsMenu(menu);
	}
	
	/** Defines a default share intent to initialize the action provider.
	  * However, as soon as the actual content to be used in the intent
	  * is known or changes, you must update the share intent by again calling
	  * mShareActionProvider.setShareIntent()
	  */
	private Intent getShareIntent() {

		RssItem rssItem;

		if (dataSingleton.nowCategory == MyConstants.NO_DATA
				|| dataSingleton.nowItem == MyConstants.NO_DATA
				|| dataSingleton.categories.size() == 0) {
			return null;
		}

		if (dataSingleton.nowCategory == MyConstants.TOP_STORIES) {
			rssItem = dataSingleton.topStories.get(dataSingleton.nowItem);
		}
		else {
			rssItem = dataSingleton.categories
					.get(dataSingleton.nowCategory)
					.getFeed(dataSingleton.nowFeed)
					.getItem(dataSingleton.nowItem);
		} 

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
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menuItemShareItem = menu.findItem(R.id.action_shareFeedItem);
		menuItemAddFeed = menu.findItem(R.id.action_addFeed);
		menuItemSettings = menu.findItem(R.id.action_settings); 
		menuItemRefresh = menu.findItem(R.id.action_refresh);
		menuItemViewOnWeb = menu.findItem(R.id.action_viewOnWeb);
		menuItemHelp = menu.findItem(R.id.action_help);
		
		/*
		 * LISTA NOTICIAS
		 * 	Drawer CERRADA
		 *   - actualizar
		 *   
		 *   Drawer ABIERTA
		 *   - Opciones
		 *   - Add feed
		 *   
		 * MOSTRAR NOTICIA
		 *  Drawer CERRADA
		 *   - Compartir
		 *   
		 *  Drawer ABIERTA
		 *   - Opciones
		 *   - Add feed
		 */
		
		
		// Men� contextual
		if (mDrawerLayout.isDrawerOpen(mDrawer)) {
			// Navigation drawer abierta
			
			menuItemAddFeed
			.setEnabled(true)
			.setVisible(true);
			
			menuItemShareItem
			.setEnabled(false)
			.setVisible(false);
			
			menuItemSettings
			.setEnabled(true)
			.setVisible(true);
			
			menuItemRefresh
			.setEnabled(false)
			.setVisible(false);
			
			menuItemViewOnWeb
			.setEnabled(false)
			.setVisible(false);
			
			menuItemHelp
			.setEnabled(true)
			.setVisible(true);
			
		} else {
			// Navigation drawer cerrada
			
			// Ocultar acci�n "Add Feed"
			menuItemAddFeed
			.setEnabled(false)
			.setVisible(false);
			
			menuItemSettings
			.setEnabled(false)
			.setVisible(false);
			
			menuItemHelp
			.setEnabled(false)
			.setVisible(false);
			
			// Permanecer oculto en carga inicial
			if (dataSingleton._isLoaded) {
				menuItemRefresh
				.setEnabled(true)
				.setVisible(true);
			}
			
			// Si se est� mostrando un item, mostrar compartir
			if (dataSingleton.nowItem != MyConstants.NO_DATA) {
				menuItemShareItem
				.setEnabled(true)
				.setVisible(true);
				
				menuItemViewOnWeb
				.setEnabled(true)
				.setVisible(true);
				
			// TODO: esto no ser�a necesario, llegado a este punto la opci�n
			//		 deber�a estar ya oculta.
			} else {
				menuItemShareItem
				.setEnabled(false)
				.setVisible(false);
				
				menuItemViewOnWeb
				.setEnabled(false)
				.setVisible(false);
			}
			
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch(item.getItemId()) {
		case R.id.action_viewOnWeb:
			// crear intent para ver noticia en web
			Uri uri = Uri.parse(
					dataSingleton.categories
					.get(dataSingleton.nowCategory)
					.getFeed(dataSingleton.nowFeed)
					.getItem(dataSingleton.nowItem)
					.getLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
			}
			return true;


		case R.id.action_addFeed:
			onAddFeed ();
			return true;

		case R.id.action_refresh:
			if (dataSingleton.nowCategory == MyConstants.TOP_STORIES) {
				refreshFeeds();
			} else {
				refreshFeed(dataSingleton.nowCategory, dataSingleton.nowFeed);
			}
			/* icono refresh animado */
			setRefresh (true);
			return true;
			
		case R.id.action_settings:
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));
			return true;
		
		case R.id.action_help:
			startActivity(new Intent(MainActivity.this, HelpActivity.class));
			return true;
		}
			
		
		return super.onOptionsItemSelected(item);
	}
	/* --------------------------------------------------------------------- */
	
	
	
	
	
	
	

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		// Pass any configuration change to the drawer toggle
		mDrawerToggle.onConfigurationChanged(newConfig);
		
	}

	
	
	
	/* ------------- XmlLoadInterface -------------------------------------- */
	@Override
	public void onXmlLoadStart(int category, int feed, int error) {
		
	}
	
	@Override
	public void onXmlLoadProgress (int category, int feed, int error) {

		int progress = 0;
		// porcentaje de carga
		if (dataSingleton.feedTotalCount-dataSingleton.feedCount != 0) {
			progress = ((dataSingleton.feedTotalCount-dataSingleton.feedCount)*100)/dataSingleton.feedTotalCount;
		}
		
		
		// Notificar error cargando feed
		if (error == MyConstants.ERROR) {
			Toast.makeText(
					dataSingleton.context,
					"Error cargando feed " + dataSingleton.categories.get(category).getFeed(feed).getTitle(),
					Toast.LENGTH_LONG)
				.show();
			return;
		}
		
		// Mostrar nombre del feed cargado
		if (getSupportFragmentManager().findFragmentByTag("fragmentLoadingScreen") != null) {
			
			((FragmentLoadingScreen) fragmentLoadingScreen)
			.showProgress(progress, dataSingleton.categories.get(category).getFeed(feed).getTitle());
		}
		
		

	}
	
	@Override
	public void onXmlLoadComplete(int category, int feed, int error)
	{
		

		// Se llama al m�todo por primera vez en la ejecuci�n del programa
		fragmentLoadingScreen = getSupportFragmentManager().findFragmentByTag("fragmentLoadingScreen");
		if (fragmentLoadingScreen != null) {
			if (fragmentLoadingScreen.isAdded()) {

				dataSingleton.feedCount--;

				// Si se carga el �ltimo, reemplazar el fragment por la lista de feeds.
				if (dataSingleton.feedCount == 0) {

					// Notificar construcci�n de lista relevantes
					((FragmentLoadingScreen) fragmentLoadingScreen).showProgress(100, "Construyendo lista de relevantes");
					
					// Construir lista de relevantes
					dataSingleton.topStories = relevantHistoryMaker.buildList();

					// Categor�a actual, destacadas
					dataSingleton.nowCategory = MyConstants.TOP_STORIES;
					
					// Recuperar la orientaci�n de pantalla controlada por sensor
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

					Utils.MyLog('d', "Fragment transaction: fragmentLoadingScreen -> fragmentFeedItemList");

					if (fragmentFeedItemList == null) {
						Utils.MyLog('d', "Nuevo fragmentFeedItemList");
						fragmentFeedItemList = new FragmentFeedItemList();
					}

					/* Carga del fragment feed item list */
					// TODO: puedo instanciar el fragment con argumentos, sera util a la hora de comunicarme con ellos
					fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(fragmentManager.findFragmentByTag("fragmentLoadingScreen").getId(), fragmentFeedItemList, "fragmentFeedItemList");
					fragmentTransaction.commit();

					//getActionBar().setDisplayHomeAsUpEnabled(true);
			        getActionBar().setHomeButtonEnabled(true);

					setTitle (getResources().getString(R.string.string_topStories));

					// desbloquear navigation drawer
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); 
					
					// Mostrar menu refrescar
					menuItemRefresh
					.setEnabled(true)
					.setVisible(true);

				}

				// Indicar que se ha cargado la informaci�n
				dataSingleton._isLoaded = true;

				return;
			}
		}
		
		// Se llama al m�todo durante la ejecuci�n del programa
		fragmentFeedItemList = getSupportFragmentManager().findFragmentByTag("fragmentFeedItemList");
		if (fragmentFeedItemList != null) {
			if (fragmentFeedItemList.isAdded()) {
				// Construir lista de relevantes
				dataSingleton.topStories = relevantHistoryMaker.buildList();
				
				dataSingleton.feedCount--;
				
				if (dataSingleton.feedCount == 0) {
					/* icono refresh est�tico */
					setRefresh (false);
				}
				

				// Recuperar la orientaci�n de pantalla controlada por sensor
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
				
				// Notificar actualizaci�n
				if (error == MyConstants.ERROR) {
					Toast.makeText(dataSingleton.context, "Error actualizando " + dataSingleton.categories.get(category).getFeed(feed).getTitle(), Toast.LENGTH_LONG).show();
				} else {
					// TODO: una sola notificaci�n cuando se actualizan varios
					Toast.makeText(dataSingleton.context, "Actualizado " + dataSingleton.categories.get(category).getFeed(feed).getTitle(), Toast.LENGTH_SHORT).show();
				 }

				// Reaccionar al cambio de datos
				if (dataSingleton.nowCategory == MyConstants.TOP_STORIES) {
					((FragmentFeedItemList) fragmentFeedItemList).mostrarLista(dataSingleton.topStories);
					
				} else {
					((FragmentFeedItemList) fragmentFeedItemList).mostrarLista(dataSingleton.categories.get(dataSingleton.nowCategory).getFeed(dataSingleton.nowFeed).getItems());

				}
				
				return;
			}
		}
		
	}
	/* --------------------------------------------------------------------- */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* The click listener for Add Feed button in the navigation drawer ----- */
	private class BtRelevantOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			dataSingleton.nowCategory = MyConstants.TOP_STORIES;
			dataSingleton.nowFeed = MyConstants.NO_DATA;
			dataSingleton.nowItem = MyConstants.NO_DATA;

			// Si hay fragmentFeedItem
			fragmentFeedItem = fragmentManager.findFragmentByTag("fragmentFeedItem");
			if (fragmentFeedItem != null) {
				if (fragmentFeedItem.isAdded()) {
					Utils.MyLog('d',  "Vaciando contenido en FragmentFeedItem");
					((FragmentFeedItem) fragmentFeedItem).emptyItem();
				}
			}

			mTitle = getResources().getString(R.string.string_topStories);
			((FragmentFeedItemList) fragmentFeedItemList).mostrarLista(dataSingleton.topStories);
			mDrawerLayout.closeDrawer(mDrawer);
		}

	}
	/* --------------------------------------------------------------------- */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    /* The click listener for ListView in the navigation drawer ------------ */
    private class DrawerItemClickListener
    	implements 	ExpandableListView.OnGroupClickListener,
    				ExpandableListView.OnChildClickListener,
    				AdapterView.OnItemLongClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			//Utils.MyLog('d', "onChildClick");
			
			// establecer feed actual (respecto a su categor�a)
			dataSingleton.nowCategory = groupPosition;
			dataSingleton.nowFeed = childPosition;
			dataSingleton.nowItem = MyConstants.NO_DATA;
			
			fragmentFeedItemList = fragmentManager.findFragmentByTag("fragmentFeedItemList");
			// Mostrar lista de items del feed seleccionado
			Log.d ("Atryl", "" +groupPosition + childPosition);
			((FragmentFeedItemList) fragmentFeedItemList).mostrarLista(dataSingleton.categories.get(groupPosition).getFeed(childPosition).getItems());
			
			// Si hay fragmentFeedItem
			fragmentFeedItem = fragmentManager.findFragmentByTag("fragmentFeedItem");
			if (fragmentFeedItem != null) {
				if (fragmentFeedItem.isAdded()) {
					Utils.MyLog('d',  "Vaciando contenido en FragmentFeedItem");
					((FragmentFeedItem) fragmentFeedItem).emptyItem();
				}
			}
			
			// update selected item and title, then close the drawer
			int index = mDrawerList.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
			mDrawerList.setItemChecked(index, true);
	        setTitle(dataSingleton.categories.get(groupPosition).getFeed(childPosition).getTitle());
	        mDrawerLayout.closeDrawer(mDrawer);
	        
			return true;
		}

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			
			// establecer categor�a actual
			dataSingleton.nowCategory = groupPosition;

			return false; // no se ha manejado el click (expande la categor�a)
		}
		
		
		/** Editar suscripciones ya existentes */
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        	int childPosition, groupPosition;
        	boolean retVal=true;
            int itemType = ExpandableListView.getPackedPositionType(id);
            

            if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                childPosition = ExpandableListView.getPackedPositionChild(id);
                groupPosition = ExpandableListView.getPackedPositionGroup(id);
                
                //do your per-item callback here
                onFeedEdit (groupPosition, childPosition);
                
                return retVal; //true if we consumed the click, false if not

            } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                groupPosition = ExpandableListView.getPackedPositionGroup(id);
                
                //do your per-group callback here
                onCategoryEdit (groupPosition);
                
                return retVal; //true if we consumed the click, false if not

            } else {
                // null item; we don't consume the click
                return false;
            }
        }

    }
    /* --------------------------------------------------------------------- */
    


    

    /* Gesti�n de di�logos ------------------------------------------------- */
    
    /* Mostrar di�logo "Editar categor�a" */
    public void onCategoryEdit (int category) {

    	// Create an instance of the dialog fragment and show it
    	DialogFragment dialog = DialogEditCategory.newInstance(category);
        dialog.show(getSupportFragmentManager(), "DialogEditCategory");

    }
    
    
    /* Mostrar di�logo "Editar Feed" */
    public void onFeedEdit (int category, int feed) {

    	// Create an instance of the dialog fragment and show it
    	DialogFragment dialog = DialogEditFeed.newInstance(category, feed);
        dialog.show(getSupportFragmentManager(), "DialogEditFeed");

    }
    
    
    /* Mostrar di�logo "A�adir feed" */
    public void onAddFeed () {
    	
    	// Create an instance of the dialog fragment and show it
    	DialogFragment dialog = new DialogAddFeed();
        dialog.show(getSupportFragmentManager(), "DialogAddFeed");
        
    }
    

    /* The dialog fragment receives a reference to this Activity through the
     * Fragment.onAttach() callback, which it uses to call the following methods
     * defined by the NoticeDialogFragment.NoticeDialogListener interface
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
    	
    	// User touched the dialog's positive button
    	
    	// Editar categor�a
    	if (dialog.getTag().equals("DialogEditCategory")) {
    		int category = ((DialogEditCategory) dialog).getCategory();
    		String categoryNewName = ((DialogEditCategory) dialog).getCategoryNewName();

    		if (categoryNewName != null) {
    			dataSingleton.categories.get(category).setName(categoryNewName);
    			
    			// Persistencia
    			almacen.setFeedsStruct(dataSingleton.categories);
    			
    			Toast.makeText(getApplicationContext(), "Categor�a editada", Toast.LENGTH_SHORT).show();
    		} else {
    			Toast.makeText(getApplicationContext(), "Sin cambios", Toast.LENGTH_SHORT).show();
    		}
    		
    	// Editar feed
    	} else if(dialog.getTag().equals("DialogEditFeed")) { 
    		int category = ((DialogEditFeed) dialog).getCategory();
    		int feed = ((DialogEditFeed) dialog).getFeed();
    		String feedNewTitle = ((DialogEditFeed) dialog).getFeedNewTitle();
    		String feedNewDescription = ((DialogEditFeed) dialog).getFeedNewDescription();
    		
    		if (feedNewTitle != null || feedNewDescription != null) {
    			if (feedNewTitle != null) {
    				dataSingleton.categories.get(category).getFeed(feed).setTitle(feedNewTitle);
    			}
    			
    			if (feedNewDescription != null) {
    				dataSingleton.categories.get(category).getFeed(feed).setDescription(feedNewDescription);
    			}

    			// Persistencia
    			almacen.setFeedsStruct(dataSingleton.categories);
    		} else {
    			Toast.makeText(getApplicationContext(), "Sin cambios", Toast.LENGTH_SHORT).show();
    		}
    		
    	// A�adir feed
    	} else if (dialog.getTag().equals("DialogAddFeed")) {
    		// A�adir nuevo feed
    		addNewFeed ((DialogAddFeed) dialog);
    	} else {
    		Utils.MyLog('d', "Dialogo no implementado");
    	}
    }
    
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }
    
    @Override
    public void onDialogNeutralClick (DialogFragment dialog) {
    	// User touched the dialog's neutral button
    	
    	// Eliminar Feed
    	if (dialog.getTag().equals("DialogEditFeed")) {
    		deleteFeed ((DialogEditFeed) dialog);
    	} else {
    		Utils.MyLog('d', "Di�logo no implementado");
    	}
    }
    /* --------------------------------------------------------------------- */

    
    
    
    
    
	/* Comunicacion FragmentFeedItemList ----------------------------------- */
        
    /* Se recibe evento Item Click de la lista de noticias
     * (non-Javadoc)
     * @see com.manumuve.atryl.fragment.FragmentFeedItemList.OnFeedItemListSelectedListener#onFeedItemListSelected(com.manumuve.atryl.RssItem)
     */
	@Override
	public void onFeedItemListSelected(RssItem rssItem) {
		
		mostrarItem (rssItem);

	}
	/* --------------------------------------------------------------------- */

	
	
	
	/* Gesti�n de suscripciones -------------------------------------------- */
	
	/* A�adir feed */
	public int addNewFeed (DialogAddFeed dialog) {

		String newRssCategoryName;
		RssFeed newRssFeed;
		int newRssFeedCategory;

		newRssFeed = dialog.getNewRssFeed();

		// Error
		if (newRssFeed == null) {
			Utils.MyLog('d', "addNewFeed: null feed");
			return -1;
		}

		newRssCategoryName = dialog.getNewRssCategoryName();

		// Si no hay categor�as ni se define una nueva
		if (dataSingleton.categories.size() == 0 && newRssCategoryName.length() == 0) {
			//TODO: string
			Toast.makeText(getApplicationContext(), "No se ha definido una categor�a", Toast.LENGTH_SHORT).show();

			return -1;
		}

		// Si se agrega en categor�a nueva
		if (newRssCategoryName.length() > 0) {
			RssCategory newRssCategory = new RssCategory();
			newRssCategory.setName(newRssCategoryName);
			newRssCategory.addFeed(newRssFeed);
			dataSingleton.categories.add(newRssCategory);
			((BaseExpandableListAdapter) mDrawerList.getExpandableListAdapter()).notifyDataSetChanged();
			Utils.MyLog('d', "addNewFeed: " + newRssFeed.getTitle() + " added in new category " + newRssCategoryName);
		}
		// Si se agrega en categor�a existente
		else {
			newRssFeedCategory = dialog.getNewRssFeedCategory();
			dataSingleton.categories.get(newRssFeedCategory).addFeed(newRssFeed);
			((BaseExpandableListAdapter) mDrawerList.getExpandableListAdapter()).notifyDataSetChanged();
			Utils.MyLog('d', "addNewFeed: " + newRssFeed.getTitle() + " added in already created category " + dataSingleton.categories.get(newRssFeedCategory).getName());
		}

		almacen.setFeedsStruct(dataSingleton.categories);
		
		dataSingleton.feedTotalCount++;
		dataSingleton._isLoaded = true;

		if (btTopStories.getVisibility() == View.GONE)
			btTopStories.setVisibility(View.VISIBLE);
		
		// Si hay fragmentHelp, cargar fragmentFeedItemList
		if (fragmentHelp != null) {
			if (fragmentHelp.isAdded()) {
				// solo hay una categor�a y un feed en este momento
				dataSingleton.nowCategory = 0;
				dataSingleton.nowFeed = 0;
				fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(fragmentManager.findFragmentByTag("fragmentHelp").getId(), fragmentFeedItemList, "fragmentFeedItemList");
				fragmentTransaction.commit();
			}
		}
		

		return 0; // ejecuci�n sin error
	}
	
	/* Borrar feed */
	public int deleteFeed (DialogEditFeed dialog) {
		
		if (dialog.getDeleteFeed() == false) {
			//TODO: string
			Toast.makeText(getApplicationContext(), "No se ha eliminado el feed", Toast.LENGTH_SHORT).show();
			
			return -1;
			
		} else {
			
			((FragmentFeedItemList) fragmentFeedItemList).mostrarLista(dataSingleton.categories.get(0).getFeed(0).getItems());
			
			dataSingleton.categories.get(dialog.getCategory()).feeds.remove(dialog.getFeed());
			
			// Notificar cambios
			((BaseExpandableListAdapter) mDrawerList.getExpandableListAdapter()).notifyDataSetChanged();
			
			Utils.MyLog('d', "Feed eliminado");
			
			if (dataSingleton.categories.get(dialog.getCategory()).getFeeds().isEmpty()) {
				dataSingleton.categories.remove(dialog.getCategory());
				dataSingleton.feedTotalCount--;
				
				
				// Notificar cambios
				((BaseExpandableListAdapter) mDrawerList.getExpandableListAdapter()).notifyDataSetChanged();
				
				Utils.MyLog('d', "Categor�a vac�a eliminada");
				
				// Se elimina la �ltima categor�a que quedaba
				// sustituir fragmentFeedItemList por �Bienvenido!
				if (dataSingleton.categories.size() == 0) {
					
					dataSingleton._isLoaded = false;
					dataSingleton.nowCategory = MyConstants.NO_DATA;
					dataSingleton.nowFeed = MyConstants.NO_DATA;
					dataSingleton.nowItem = MyConstants.NO_DATA;
					
					fragmentHelp = new FragmentHelp();
					if (fragmentHelp != null) {
						Log.d("ATRYL", "reemplazar fragmentFeedItemList por fragmentHelp");
						fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction
						.replace(fragmentManager.findFragmentByTag("fragmentFeedItemList").getId(), fragmentHelp, "fragmentHelp");
						//.remove(fragmentFeedItemList);
						fragmentTransaction.commit();
					}
					
					// Vaciar fragmentFeedItem
					if (fragmentFeedItem != null) {
						if (fragmentFeedItem.isAdded()) {
							Utils.MyLog('d',  "Vaciando contenido en FragmentFeedItem");
							((FragmentFeedItem) fragmentFeedItem).emptyItem();
						}
					}
				}
			}
			
			almacen.setFeedsStruct(dataSingleton.categories);
			
		}
		
		return 0; // ejecuci�n sin error
	}
	
	
	/* Cargar feeds desde Internet */
	public void refreshFeeds () {
		
		// Bloquear cambios en la orientaci�n de la pantalla
		/* TODO: Se puede mejorar permitiendo cambiar la orientaci�n de
		 * la pantalla sin que las asynctask pierdan la referencia
		 * 
		 * 
		 */
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		XmlLoad xmlLoad;
		
		dataSingleton.feedCount = 0;
		dataSingleton.feedTotalCount = 0;
		
		for (int i=0; i<dataSingleton.categories.size(); i++) {
			for (int j=0; j<dataSingleton.categories.get(i).getFeeds().size(); j++) {
				dataSingleton.feedCount++;
				dataSingleton.feedTotalCount++;
				// Sobre tareas as�ncronas en paralelo:
				// https://github.com/vitkhudenko/test_asynctask/
				xmlLoad = new XmlLoad (this, i, j);
				//xmlLoad.execute(); si se quieren cargar en serie
				xmlLoad.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				Utils.MyLog('d', "xmlLoad: "+ dataSingleton.categories.get(i).getFeed(j).getTitle());
			}
		}
	}
	
	
	/* Cargar un �nico feed desde Internet */
	public void refreshFeed (int category, int feed) {
		
		// Bloquear cambios en la orientaci�n de la pantalla
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		XmlLoad xmlLoad;
		
		dataSingleton.feedCount = 1;
		
		xmlLoad = new XmlLoad (this, category, feed);
		
		// Tarea as�ncrona, ejecuci�n en serie
		// xmlLoad.execute();
		
		// Tarea as�ncrona, ejecuci�n en paralelo
		xmlLoad.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		Utils.MyLog('d', "xmlLoad: "+ dataSingleton.categories.get(category).getFeed(feed).getTitle());
		
	}
	
	/* --------------------------------------------------------------------- */
	
	
	
	
	/* M�todos propios de la clase ----------------------------------------- */
	
	/* Decidir m�todo de persistencia del panel */
	public int metodoAlmacen (int metodo) {

		switch (metodo) {
		case (MyConstants.SAX_PARSER):
			almacen = new AlmacenStructXML_SAX(this);
			return MyConstants.NO_ERROR;
		
		default:
			return MyConstants.ERROR;
		}

	}
	
	/* Mostrar el item seleccionado */
	public void mostrarItem (RssItem rssItem) {
		
		fragmentFeedItem = fragmentManager.findFragmentByTag("fragmentFeedItem");
		if (fragmentFeedItem != null) {
			if (fragmentFeedItem.isAdded()) {
				Utils.MyLog('v', "Cargar RssItem en fragmentFeedItem");
				Log.d("Atryl", "Cargar RssItem en fragmentFeedItem");
				((FragmentFeedItem)getSupportFragmentManager().findFragmentById(R.id.fragmentFeedItem)).cargarRssItem(rssItem);
				((FragmentFeedItem)getSupportFragmentManager().findFragmentById(R.id.fragmentFeedItem)).mostrarRssItem();
				
				// Mostrar men� compartir
				invalidateOptionsMenu();
				
				return;
			}
		}
		
		Utils.MyLog('v', "Cargar RssItem en FeedItemActivity");
		FeedItemActivity.setRssItem(rssItem);
		Intent i = new Intent(this, FeedItemActivity.class);
		startActivity(i);

	}
	
	/** Indeterminate progressbar in menu
	 *	Ver funciones XmlLoad para la llamada
	 */
	public void setRefresh(boolean refresh) {
		
		if (refresh){
			menuItemRefresh.setActionView (R.layout.actionbar_indeterminate_progress);
		}else{
			menuItemRefresh.setActionView(null);
		}
	}
	 
	/* --------------------------------------------------------------------- */
	
	
	
	
}
