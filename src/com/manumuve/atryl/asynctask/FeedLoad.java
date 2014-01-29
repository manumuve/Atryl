package com.manumuve.atryl.asynctask;

import java.io.IOException;
import java.net.URL;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.manumuve.atryl.data.RssFeed;
import com.manumuve.atryl.data.RssReader;



/**
 * Tarea asíncrona para cargar un feed
 * Los argumentos que indican los datos a actualizar los recibe
 * en el constructor
 * 	Sin categoría ni feed, actualiza todo el contenido.
 * 	Solo con categoria, actualiza los feeds de la categoría indicada
 * 	Con feed, actualiza el feed indicado
 * http://www.sgoliver.net/blog/?p=3099
 * @author Manu
 *
 */
public class FeedLoad extends AsyncTask<URL, Void, RssFeed> /* Params, Progress, Result */ {

	/* -------------------------------------------------------
     * Comprobar que la clase que hace la llamada implementa
     * la interfaz necesaria para los eventos callback
     * -------------------------------------------------------
     */
	// Use this instance of the interface to deliver action events
	FeedLoadInterface mListener;

	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			mListener = (FeedLoadInterface) caller;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(caller.toString()
					+ " must implement NoticeDialogListener");
		}

	}
	/* -------------------------------------------------------  */

	
	private Object caller; // TODO: declararlo como object puede consumir demasiada memoria
	private RssFeed rssFeed;
	URL url;
	

	/* Constructor público que toma los argumentos con los que trabajar
	 * Llamada desde Activity
	 */
	public FeedLoad (Activity act, URL url) {
		caller = act;
		this.url = url;
		rssFeed = new RssFeed();
	}
	
	/* Constructor público que toma los argumentos con los que trabajar
	 * Llamada desde Fragment
	 */
	public FeedLoad (Fragment frg, URL url) {
		caller = frg;
		this.url = url;
		rssFeed = new RssFeed();
	}
	
	/* Constructor público que toma los argumentos con los que trabajar
	 * Llamada desde DialogFragment
	 */
	public FeedLoad (DialogFragment dlg, URL url) {
		caller = dlg;
		this.url = url;
		rssFeed = new RssFeed();
	}

	@Override
	protected RssFeed doInBackground (URL... url) {

		try {
			// Cargar feed
			rssFeed = RssReader.read (url[0]);
			if (rssFeed != null)
				if (rssFeed.isValidFeed())
					return rssFeed;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return null;

	}

	@Override
	protected void onPostExecute (RssFeed result) {
		mListener.onFeedLoadComplete(result);
	}

}
