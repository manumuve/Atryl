package com.manumuve.atryl.data;

import java.util.ArrayList;

import android.content.Context;

import com.manumuve.atryl.util.MyConstants;

/**
 * Patrón Singleton para centralizar los datos de la aplicación
 * http://es.wikipedia.org/wiki/Singleton
 * http://en.wikipedia.org/wiki/Singleton_pattern
 * 
 * Clase diseñada siguiendo el patrón Singleton: El patrón de diseño singleton
 * (instancia única) está diseñdo para restringir la creación de objetos
 * pertenecientes a una clase o el valor de un tipo a un único objeto. Su
 * intención consiste en garantizar que una clase solo tenga una instancia y
 * proporcionar un punto de acceso global a ella.
 * 
 * @author Manumuve
 * @version 0.3
 */
public class DataSingleton {

	private static DataSingleton _instance;

	/** Colección de datos de la aplicación */
	public ArrayList<RssCategory> categories;

	/** Lista de noticias destacadas */
	public ArrayList<RssItem> topStories;

	/** Contexto de la aplicación */
	public Context context;

	/** Flag para determinar el tipo de dispositivo */
	public boolean _isSmartphone;

	/** Estado actual de la ejecución */
	public Boolean _isLoaded; // hay datos cargados en el Singleton
	public int nowCategory; // categoría actual
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
	 * Getter de la instancia estática de de clase
	 * 
	 * @return static DataSingleton: instancia estática de la clase
	 */
	public static DataSingleton getInstance() {
		if (_instance == null) {
			_instance = new DataSingleton();
		}

		return _instance;
	}

	/**
	 * Obtener categoría por nombre
	 * 
	 * @return int: posición de la categoría buscada o null si no se encuentra.
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
