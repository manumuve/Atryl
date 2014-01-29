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
import android.widget.Toast;

import com.manumuve.atryl.activity.MainActivity;
import com.manumuve.atryl.data.DataSingleton;
import com.manumuve.atryl.data.RssCategory;


public class Utils {
	 
	static DataSingleton dataSingleton = DataSingleton.getInstance();

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
	
	/* Comprobar si existe conexión a Internet.
	 * Se puede comprobar de qué tipo es la conexión, si existe, mediante:
	 * info.getType() -> ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_WIMAX…
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
	
	/* Comprobar si una URL está online o no
	 * true o false dependiendo del resultado
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Boolean.valueOf(false);
		
	}
	
	
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
	 * Returns true if the current device is a smartphone or a "tabletphone"
	 * like Samsung Galaxy Note or false if not.
	 * A Smartphone is "a device with less than TABLET_MIN_DP_WEIGHT" dpi
	 * 
	 * @return true if the current device is a smartphone or false in other 
	 * case
	 * Ref: http://stackoverflow.com/questions/7511330/how-to-detect-a-tablet-device-in-android
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

		if (dpi < MyConstants.TABLET_MIN_DP_WEIGHT) return true;
		else                                        return false;
	}



	/////////////////////// BASURA ///////////////////////
	static public void rellenotemporal () {
		//dataSingleton.categories = new ArrayList<RssCategory>();
		Toast.makeText(dataSingleton.context, "First run del programa o sin datos", Toast.LENGTH_SHORT).show();
		RssCategory category = new RssCategory();
		category.setName("Noticias");
		category.addFeed("Europa Press", "Agencia Europa Press", Utils.stringToURL("http://www.europapress.es/rss/rss.aspx?ch=94"));
		category.addFeed("El diario digital de Ávila", "El diario digital de Ávila", Utils.stringToURL("http://avilared.com/rss.xml"));
		category.addFeed("Tribuna de Ávila", "Tribuna de Ávila > Noticias", Utils.stringToURL("http://www.tribunaavila.com/noticias/rss.rss"));
		dataSingleton.categories.add(category);
		
		category = new RssCategory();
		category.setName("Tecnología");
		category.addFeed("Engadget en español", "Engadget en español", Utils.stringToURL("http://feeds.feedburner.com/EngadgetSpanish?format=xml"));
		category.addFeed("El Androide Libre", "Android blog - Comunidad Web - Aplicaciones, juegos, smartphones: Información", Utils.stringToURL("http://feeds.feedburner.com/elandroidelibre?format=xml"));
		category.addFeed("ElOtroLado.net", "Consolas, videojuegos, nuevas tecnologías y actualidad internauta - Xbox One PS4 Wii U", Utils.stringToURL("http://www.elotrolado.net/feed/"));
		dataSingleton.categories.add(category);
		
		MainActivity.almacen.setFeedsStruct(dataSingleton.categories);
		
	}
	
	static public void informaciontemporal () {
		if (dataSingleton._isLoaded) {
			Utils.MyLog('d', "dataSingleton cargado con "+dataSingleton.categories.size()+" categorías");
		} else {
			Utils.MyLog('d', "dataSingleton no contiene datos");
		}
			
	}
	
	
}
