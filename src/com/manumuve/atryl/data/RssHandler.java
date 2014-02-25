/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.manumuve.atryl.util.MyConstants;
import com.manumuve.atryl.util.Utils;

/**
 * Manejador RSS.
 * Lee el contenido de un canal RSS contenido en un documento XML
 * y genera el objeto RssFeed correspondiente con todos sus RssItem.
 * @author Manu
 *
 */

public class RssHandler extends DefaultHandler {
	private RssFeed rssFeed;
	private RssItem rssItem;
	private StringBuilder stringBuilder;
	private String methodName;
	private boolean _validFeed;
	private Context context = DataSingleton.getInstance().context;
	private EasyTracker myTracker = EasyTracker.getInstance(context);
	private ArrayList<String> xmlParseExceptions = new ArrayList<String>();

	/**
	 * Método de inicio de documento.
	 * Inicializa las variables a utilizar.
	 */
	@Override
	public void startDocument() throws SAXException {

		super.startDocument();

		rssFeed = new RssFeed();
		stringBuilder = new StringBuilder();
		_validFeed = false;
		
	}

	/**
	 * Método de inicio de elemento.
	 * Crea un nuevo RssItem y lo añade al RssFeed
	 * que se está procesando.
	 */
	@Override
	public void startElement (String uri, String localName, String qName, Attributes attributes) {
		stringBuilder = new StringBuilder();
		if(qName.equals("item") && rssFeed != null) {
			rssItem = new RssItem();
			//rssItem.setFeed(rssFeed);
			rssFeed.addItem(rssItem);
		}
	}
	
	/**
	 * Método de contenido de elemento.
	 * Almacena el contenido del elemento que se
	 * está analizando.
	 */
	@Override
	public void characters (char[] ch, int start, int length) {

		stringBuilder.append (ch, start, length);
		
	}
	
	/**
	 * Método de final de elemento.
	 * Realiza llamadas a los setters de RssFeed y RssItem
	 * para establecer los valores de los atributos leídos.
	 * Genera nombre del método dinámicamente.
	 * Concepto:
	 * @see	https://github.com/matshofman/Android-RSS-Reader-Library
	 */
	@SuppressLint("DefaultLocale")
	@Override
	public void endElement (String uri, String localName, String qName) {
		if(rssFeed != null && rssItem == null) {
			// Parse feed properties

			try {
				
				if (qName != null && qName.length() > 0) {
					
					qName = qNameCleaner (qName);
					
				    methodName = "set" + qName.substring(0, 1).toUpperCase(Locale.US) + qName.substring(1); // Locale.US to simply replace ASCII characters
				    Method method = rssFeed.getClass().getMethod(methodName, String.class);
				    method.invoke(rssFeed, stringBuilder.toString());

				}
				
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
				// Track event
				trackEvent (methodName);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}

		} else if (rssItem != null) {
			// Parse item properties

			try {
				
				qName = qNameCleaner (qName);
					
				methodName = "set" + qName.substring(0, 1).toUpperCase(Locale.US) + qName.substring(1); // Locale.US to simply replace ASCII characters
				Method method = rssItem.getClass().getMethod(methodName, String.class);
				method.invoke(rssItem, stringBuilder.toString());
				
				_validFeed = true; // detectado al menos un item, el feed es válido

			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
				// Track event
				trackEvent (methodName);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}

	}
	
	/**
	 * Devuelve el RssFeed obtenido con sus RssItems
	 * @return
	 */
	public RssFeed getResult() {
		if (_validFeed == true) {
			return rssFeed;
		}
		else {
			return null;
		}
	}
	/**
	 * Método para limpiar una cadena de caracteres de los
	 * caracteres especiales que no nos interesan.
	 * Necesaria para que el método endElement funcione
	 * correctamente.
	 * @param qName: cadena de caracteres a limpiar
	 * @return cleanQName: cadena de caracteres limpia
	 */
	private String qNameCleaner (String qName) {
		int i;
		String cleanQName = qName;
		
		// Eliminar carácter ':' y cambiar a mayúscula el inmediatamente siguiente
		i = cleanQName.indexOf(':');
		while (i != -1) {
			// coincidencia al inicio
			if (i == 0) {
				cleanQName = cleanQName.substring(1);
			// coincidencia al final
			} else if (i == (cleanQName.length()-1)) {
				cleanQName = cleanQName.substring(0, i);
			// coincidencia en posición intermedia
			} else {
				cleanQName = cleanQName.substring (0, i) 
						+ cleanQName.substring(i+1, i+2).toUpperCase(Locale.US)
						+ cleanQName.substring(i+2);
			}
			
			i = cleanQName.indexOf(':');
		}
		
		return cleanQName;
		
	}
	
	/** Event Tracking utilizando Google Analytics SDK for Android v2
	 * @param String event: evento a registrar
	 */
	private void trackEvent (String event) {
		// Si está en la lista de excepciones, no enviar evento
		if (MyConstants.XML_PARSE_EXCEPTIONS.indexOf(event) != -1)
			return;
		
		// Solo una notificación por cada event
		if (xmlParseExceptions.indexOf(event) != -1)
			return;
		
		xmlParseExceptions.add(event);

		if (MyConstants.DEBUG) {
			Utils.MyLog('d', "RssHandler.endElement: Método no implementado: " + event);
		}

		// Statistics and tracking, if enabled in preferences
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		if (sharedPref.getBoolean("preference_SendAnonStats", true) == true) {
			/**
			 * Formato del evento:
			 *  String Category
			 *  String Action
			 *  String Label
			 *  Long (Optional) Value
			 */
			myTracker.send(MapBuilder
					.createEvent("RssHandler", "NoSuchMethodException", event, null)
					.build()
					);
		}
	}
}
