/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.data;

import java.net.URL;
import java.util.ArrayList;

/**
 * Clase que define las categorías que el usuario
 * añade en el sistema.
 * @author Manu
 *
 */

public class RssCategory {
	private String name;
	public ArrayList<RssFeed> feeds;
	
	/**
	 * Constructor estándar.
	 */
	public RssCategory() {
		this.feeds = new ArrayList<RssFeed>();
		this.name= "Sin nombre"; // evita punteros a nulo //TODO: recurso string
	}
	
	/**
	 * Constructor parametrizado.
	 * @param name: nombre de la categoría.
	 * @param feeds: feeds que contiene la categoría.
	 */
	public RssCategory(String name, ArrayList<RssFeed> feeds) {
		if (feeds == null) {
			this.feeds = new ArrayList<RssFeed>();
		} else {
			this.feeds = feeds;
		}
		
		if (name == null) {
			this.name = "Sin nombre"; //TODO: recurso string
		} else {
			this.name = name;
		}
	}

	/**
	 *  Establecer nombre de categoria
	 * @param name: nombre
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *  Obtener nombre de categoria
	 * @return nombre
	 */
	public String getName() {
		return name;
	}
	
	/**
	 *  Añadir feed recibiendo objeto f
	 * @param feed: feed que se agrega.
	 * @return resultado de la operación
	 */
	public boolean addFeed (RssFeed feed) {
		
		if (feedExists(feed.getLink()) == true) {
			return false;
		}
		
		feeds.add(feed);
		return true;
	}
	
	/* TODO: Añadir feed directamente desde URL, el resto de campos se extraen del xml
	public void addFeed (String URL) {
		
	}
	*/
	
	/**
	 *  Añadir feed obteniendo datos del nuevo feed
	 * @param title: título del nuevo feed.
	 * @param description: descripción del nuevo feed.
	 * @param link: enlace url del nuevo feed.
	 * @return resultado de la operación.
	 */
	public boolean addFeed (String title, String description, URL link) {
		
		if (feedExists(link) == true) {
			return false;
		}
		
		RssFeed feed = new RssFeed();
		
		feed.setTitle(title);
		feed.setDescription(description);
		feed.setLink(link);
		
		feeds.add(feed);
		return true;
		
	}
	
	/**
	 *  Obtener lista de feeds
	 * @return	lista de feeds.
	 */
	public ArrayList<RssFeed> getFeeds () {
		return feeds;
	}
	
	/**
	 *  Obtener feed por índice
	 * @param index		índice del feed a obtener
	 * @return feed 	obtenido
	 */
	public RssFeed getFeed (int index) {
		return feeds.get(index);
	}
	
	/**
	 *  Obtener feed por nombre
	 * @param name	nombre del feed a obtener
	 * @return		feed obtenido
	 */
	public int getFeed (String name) {
		
		for (int i=0; i<feeds.size(); i++) {
			if (feeds.get(i).getTitle().equals(name)) {
				return i;
			}
		}
		
		return (Integer) null;
	}
	
	/**
	 *  Obtener feed por url
	 * @param url	url del feed a obtener
	 * @return		feed obtenido
	 */
	public int getFeed (URL url) {

		for (int i=0; i<feeds.size(); i++) {
			if (feeds.get(i).getLink() == url) {
				return i;
			}
		}

		return (Integer) null;
	}
	
	/**
	 *  Comprobar si ya existe el feed
	 * @param url	url del feed a comprobar
	 * @return		resultado de la operación
	 */
	public boolean feedExists (URL url) {
			
		for (int i=0; i<feeds.size(); i++) {
			if (feeds.get(i).getLink() == url) {
				return true;
			}
		}
			
		return false;
	}
	
	/**
	 *  Establecer feed por índice
	 * @param i			índice donde se agrega
	 * @param rssFeed	feed que se agrega
	 * 
	 */
	public void setFeed (int i, RssFeed rssFeed) {
		feeds.add(i, rssFeed);
	}

	/**
	 *  Obtener número de feeds
	 */
	public int getFeedCount() {
		int i = 0;
        try {
        	i = feeds.size();
        } catch (Exception e) {
        }
 
        return i;
        
    }
}
