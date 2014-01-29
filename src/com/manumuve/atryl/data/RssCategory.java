package com.manumuve.atryl.data;

import java.net.URL;
import java.util.ArrayList;

public class RssCategory {
	private String name;
	public ArrayList<RssFeed> feeds;
	
	public RssCategory() {
		this.feeds = new ArrayList<RssFeed>();
		this.name= "Sin nombre"; // evita punteros a nulo //TODO: recurso string
	}
	
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

	// Establecer nombre de categoria
	public void setName(String name) {
		this.name = name;
	}

	// Obtener nombre de categoria
	public String getName() {
		return name;
	}
	
	// Añadir feed recibiendo objeto f
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
	
	// Añadir feed obteniendo datos del nuevo feed
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
	
	// Obtener lista de feeds
	public ArrayList<RssFeed> getFeeds () {
		return feeds;
	}
	
	// Obtener feed por índice
	public RssFeed getFeed (int index) {
		return feeds.get(index);
	}
	
	// Obtener feed por nombre
	public int getFeed (String name) {
		
		for (int i=0; i<feeds.size(); i++) {
			if (feeds.get(i).getTitle().equals(name)) {
				return i;
			}
		}
		
		return (Integer) null;
	}
	
	// Obtener feed por url
		public int getFeed (URL url) {
			
			for (int i=0; i<feeds.size(); i++) {
				if (feeds.get(i).getLink() == url) {
					return i;
				}
			}
			
			return (Integer) null;
		}
	
	// Comprobar si ya existe el feed
	public boolean feedExists (URL url) {
			
		for (int i=0; i<feeds.size(); i++) {
			if (feeds.get(i).getLink() == url) {
				return true;
			}
		}
			
		return false;
	}
	
	// Establecer feed por índice
	// No es necesaria
	public void setFeed (int i, RssFeed rssFeed) {
		feeds.add(i, rssFeed);
	}

	/* Obtener número de feeds */
	public int getFeedCount() {
		int i = 0;
        try {
        	i = feeds.size();
        } catch (Exception e) {
        }
 
        return i;
        
    }
}
