/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import com.manumuve.atryl.data.DataSingleton;

/**
 * Clase Utils, contiene métodos de utilidad que se invocan en diversos puntos
 * del resto de la aplicación.
 * 
 * @author Manu
 * 
 */
public class Utils {
	 
	static DataSingleton dataSingleton = DataSingleton.getInstance();

	/**
	 * Recibe una cadena de caracteres y devuelve una variable URL
	 * @param urlString
	 * @return URL recibida como String
	 */
	static public URL stringToURL (String urlString) {
		
		try {
			URL url = new URL(urlString);
			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			url = uri.toURL();
			
			return url;
			
		} catch (MalformedURLException e) {
		} catch (URISyntaxException e1) {
		}

		return null;

	}
	
	/**
	 * Comprobar si existe conexión a Internet en el momento de ser invocada.
	 * Se puede comprobar de qué tipo es la conexión, si existe, mediante:
	 * info.getType()
	 * 		ConnectivityManager.TYPE_MOBILE
	 * 		ConnectivityManager.TYPE_WIFI
	 * 		ConnectivityManager.TYPE_WIMAX
	 * 		…
	 * 
	 * @return true si existe, false si no existe.
	 */
	static public boolean checkConnectivity()
    {
        boolean enabled = true;
 
        ConnectivityManager connectivityManager = (ConnectivityManager) dataSingleton.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
         
        if ((info == null || !info.isConnected() || !info.isAvailable()))
        {
            enabled = false;
        }
        return enabled;         
    }
	
	/** 
	 * Comprobar si una URL está online en el momento de ser invocada.
	 * 
	 * @return true si está online, false si no lo está.
	 */
	static public boolean isOnline(URL url) {

		try {
			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
			urlc.connect();
			if (urlc.getResponseCode() == 200) {
				return Boolean.valueOf(true);
			}
		} catch (MalformedURLException e1) {
			// Captura de excepción
			e1.printStackTrace();
		} catch (IOException e) {
			// Captura de excepción
			e.printStackTrace();
		}

		return Boolean.valueOf(false);
		
	}
	
	/**
	 * Método de registro propio. Se utiliza en sustitución del
	 * método Log de Java.
	 * Se controla mediante la constante MyConstants.DEBUG (true o false)
	 * 
	 * @param mode: modo de registrar el mensaje msg.
	 * @param msg: mensaje que se registra.
	 */
	static public void MyLog (char mode, String msg) {
		if (MyConstants.DEBUG) {
			switch (mode) {
				case 'd':
					Log.d (MyConstants.APP_NAME, msg);
				break;
				
				case 'v':
					Log.v (MyConstants.APP_NAME, msg);
				break;
			}
		}
	}
	
	/**
	 * Comprueba si el dispositivo es un teléfono o una tableta.
	 * Se define teléfono como cualquier dispositivo con una densidad de pantalla
	 * (dpi) menor que MyConstants.TABLET_MIN_DP_WEIGHT
	 * 
	 * Ref: http://stackoverflow.com/questions/7511330/how-to-detect-a-tablet-device-in-android
	 * 
	 * @param act: activity desde la que se invoca.
	 * @return true si el dispositivo es un teléfono, false si no lo es. 
	 */
	public static boolean isSmartphone(Activity act) {
		DisplayMetrics metrics = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int dpi = 0;
		if (metrics.widthPixels < metrics.heightPixels) {
			dpi = (int) (metrics.widthPixels / metrics.density);
		} else {
			dpi = (int) (metrics.heightPixels / metrics.density);
		}

		if (dpi < MyConstants.TABLET_MIN_DP_WEIGHT)
			return true;
		else
			return false;
	}
	
}
