/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.asynctask;

import java.io.IOException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.data.RssFeed;
import com.manumuve.atryl.data.RssReader;
import com.manumuve.atryl.util.MyConstants;
import com.manumuve.atryl.util.Utils;


/** Tarea asíncrona para cargar feeds desde Internet
 * Los argumentos que indican los datos a actualizar los recibe
 * en el constructor
 * 	Sin categoría ni feed, actualiza todo el contenido.
 * 	Solo con categoria, actualiza los feeds de la categoría indicada
 * 	Con feed, actualiza el feed indicado
 * Referencia: http://www.sgoliver.net/blog/?p=3099
 * @author Manumuve
 * @version 0.1
 */
public class XmlLoad extends AsyncTask<Void, Void, Void> /* Params, Progress, Result */ {

	private XmlLoadInterface callback;
	private DataSingleton dataSingleton;
	private RssFeed rssFeed;
	private RssFeed oldRssFeed = null;
	private int category, feed;
	private Boolean _isLoaded;

	/**
	 * Constructor público que toma los argumentos con los que trabajar.
	 * Llamada desde Activity.
	 * @param act		activity que llama.
	 * @param category	categoría del feed.
	 * @param feed		índice del feed.
	 */
	public XmlLoad (Activity act, int category, int feed) {
		this.category = category;
		this.feed = feed;
		this.callback = (XmlLoadInterface) act;
		dataSingleton = DataSingleton.getInstance();
		rssFeed = new RssFeed();
		oldRssFeed = dataSingleton.categories.get(category).getFeed(feed);
	}
	
	/**
	 * Constructor público que toma los argumentos con los que trabajar.
	 * Llamada desde Fragment.
	 * @param act		fragment que llama.
	 * @param category	categoría del feed.
	 * @param feed		índice del feed.
	 */
	public XmlLoad (Fragment frg, int category, int feed) {
		this.category = category;
		this.feed = feed;
		this.callback = (XmlLoadInterface) frg;
		dataSingleton = DataSingleton.getInstance();
		rssFeed = new RssFeed();
	}
	
	/**
	 * Notifica el inicio de la operación.
	 */
	@Override
	protected void onPreExecute() {
		callback.onXmlLoadStart(category, feed, MyConstants.NO_ERROR);
    }

	/**
	 * Tarea asíncrona en hilo aparte.
	 * Se carga el contenido del feed desde Internet.
	 */
	@Override
	protected Void doInBackground(Void... params) {

		_isLoaded = false;
		
		try {
			// Actualizar feed
			rssFeed = RssReader.read(dataSingleton.categories.get(category).getFeed(feed).getLink());
			if (rssFeed == null) {
				return null;
			} else {
				_isLoaded = true;
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Evitar que se machaquen valores con datos erróneos
		if (oldRssFeed != null) {
			Utils.MyLog('d', "Recuperando valores: "+oldRssFeed.getTitle()+" | "+oldRssFeed.getDescription());
			rssFeed.setTitle(oldRssFeed.getTitle());
			rssFeed.setDescription(oldRssFeed.getDescription());
			rssFeed.setLink(oldRssFeed.getLink());
		}

		dataSingleton.categories.get(category).getFeeds().set(feed, rssFeed);
		
		publishProgress((Void[])null);
		
		return null;

	}

	/**
	 * Notifica el progreso de la operación.
	 */
	@Override
	protected void onProgressUpdate (Void...voids) {
        if (_isLoaded == true) {
        	callback.onXmlLoadProgress(category, feed, MyConstants.NO_ERROR);
        }
        else {
        	callback.onXmlLoadProgress(category, feed, MyConstants.ERROR);
        }
    }

	/**
	 * Fin de tarea asíncrona.
	 * Notifica el final de la operación y devuelve el feed obtenido.
	 */
	@Override
	protected void onPostExecute (Void result) {
		if (_isLoaded == true) {
			callback.onXmlLoadComplete(category, feed, MyConstants.NO_ERROR);

        }
        else {
        	callback.onXmlLoadComplete(category, feed, MyConstants.ERROR);
        }
		
	}

	

}
