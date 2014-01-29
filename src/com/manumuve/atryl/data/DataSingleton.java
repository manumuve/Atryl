package com.manumuve.atryl.data;

import java.util.ArrayList;

import android.content.Context;

import com.manumuve.atryl.util.MyConstants;

/**
 * Patr�n Singleton para centralizar los datos de la aplicaci�n
 * http://es.wikipedia.org/wiki/Singleton
 * http://en.wikipedia.org/wiki/Singleton_pattern
 * 
 * Clase dise�ada siguiendo el patr�n Singleton: El patr�n de dise�o singleton
 * (instancia �nica) est� dise�do para restringir la creaci�n de objetos
 * pertenecientes a una clase o el valor de un tipo a un �nico objeto. Su
 * intenci�n consiste en garantizar que una clase solo tenga una instancia y
 * proporcionar un punto de acceso global a ella.
 * 
 * @author Manumuve
 * @version 0.3
 */
public class DataSingleton {

	private static DataSingleton _instance;

	/** Colecci�n de datos de la aplicaci�n */
	public ArrayList<RssCategory> categories;

	/** Lista de noticias destacadas */
	public ArrayList<RssItem> topStories;

	/** Contexto de la aplicaci�n */
	public Context context;

	/** Flag para determinar el tipo de dispositivo */
	public boolean _isSmartphone;

	/** Estado actual de la ejecuci�n */
	public Boolean _isLoaded; // hay datos cargados en el Singleton
	public int nowCategory; // categor�a actual
	public int nowFeed; // feed actual
	public int nowItem; // item actual
	public int nowDrawerSelectedItem; // item seleccionado en barra lateral
	public int previousOrientation;
	/** Contador de carga de feeds desde Internet */
	public int feedCount;
	public int feedTotalCount;

	/** ----------------------------- */

	/**
	 * Constructor de clase Inicia las variables
	 */
	DataSingleton() {
		categories = new ArrayList<RssCategory>();
		topStories = new ArrayList<RssItem>();
		
		/** Por defecto, asumir smartphone */
		_isSmartphone = true;
		
		/** Sin datos */
		_isLoaded = false;
		nowCategory = MyConstants.NO_DATA;
		nowFeed = MyConstants.NO_DATA;
		nowItem = MyConstants.NO_DATA;
		nowDrawerSelectedItem = MyConstants.NO_DATA;
		previousOrientation = MyConstants.NO_DATA;
		/** Contador de carga de feeds desde Internet */
		feedCount = 0;
		feedTotalCount = 0;
	}

	/**
	 * Getter de la instancia est�tica de de clase
	 * 
	 * @return static DataSingleton: instancia est�tica de la clase
	 */
	public static DataSingleton getInstance() {
		if (_instance == null) {
			_instance = new DataSingleton();
		}

		return _instance;
	}

	/**
	 * Obtener categor�a por nombre
	 * 
	 * @return int: posici�n de la categor�a buscada o null si no se encuentra.
	 */

	public int getCategory(String name) {

		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getName().equals(name)) {
				return i;
			}
		}

		return (Integer) null;
	}

}
