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

public class RssHandler extends DefaultHandler {
	private RssFeed rssFeed;
	private RssItem rssItem;
	private StringBuilder stringBuilder;
	private String methodName;
	private boolean _validFeed;
	private Context context = DataSingleton.getInstance().context;
	private EasyTracker myTracker = EasyTracker.getInstance(context);
	private ArrayList<String> xmlParseExceptions = new ArrayList<String>();

	@Override
	public void startDocument() throws SAXException {

		super.startDocument();

		rssFeed = new RssFeed();
		stringBuilder = new StringBuilder();
		_validFeed = false;
		
	}

	@Override
	public void startElement (String uri, String localName, String qName, Attributes attributes) {
		stringBuilder = new StringBuilder();
		if(qName.equals("item") && rssFeed != null) {
			rssItem = new RssItem();
//			rssItem.setFeed(rssFeed);
			rssFeed.addItem(rssItem);
		}
	}
	
	@Override
	public void characters (char[] ch, int start, int length) {

		stringBuilder.append (ch, start, length);
		
	}
	
	/**
	 * Generar nombre del m�todo din�micamente. Concepto:
	 * https://github.com/matshofman/Android-RSS-Reader-Library
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
				
				_validFeed = true; // detectado al menos un item, el feed es v�lido

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
	 * Return the parsed RssFeed with it's RssItems
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
	
	private String qNameCleaner (String qName) {
		int i;
		String cleanQName = qName;
		
		// Eliminar car�cter ':' y cambiar a may�scula el inmediatamente siguiente
		i = cleanQName.indexOf(':');
		while (i != -1) {
			// coincidencia al inicio
			if (i == 0) {
				cleanQName = cleanQName.substring(1);
			// coincidencia al final
			} else if (i == (cleanQName.length()-1)) {
				cleanQName = cleanQName.substring(0, i);
			// coincidencia en posici�n intermedia
			} else {
				cleanQName = cleanQName.substring (0, i) 
						+ cleanQName.substring(i+1, i+2).toUpperCase(Locale.US)
						+ cleanQName.substring(i+2);
			}
			
			i = cleanQName.indexOf(':');
		}
		
		return cleanQName;
		
	}
	
	/** Event Tracking using the Google Analytics SDK for Android v2
	 * @param String event: evento a registrar
	 */
	private void trackEvent (String event) {
		// Si est� en la lista de excepciones, no enviar evento
		if (MyConstants.XML_PARSE_EXCEPTIONS.indexOf(event) != -1)
			return;
		
		// Solo una notificaci�n por cada event
		if (xmlParseExceptions.indexOf(event) != -1)
			return;
		
		xmlParseExceptions.add(event);

		if (MyConstants.DEBUG) {
			Utils.MyLog('d', "RssHandler.endElement: M�todo no implementado: " + event);
		}

		// Statistics and tracking, if enabled in preferences
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		if (sharedPref.getBoolean("preference_SendAnonStats", true) == true) {
			/*
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