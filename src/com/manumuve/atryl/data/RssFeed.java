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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.manumuve.atryl.util.Utils;

/**
 * Clase que define las suscripciones que el usuario
 * añade en el sistema.
 * @author Manu
 *
 */
public class RssFeed implements Parcelable {

	private String title;
	private String description;
	private URL link;
	private ArrayList<RssItem> items;
	
	public static final Parcelable.Creator<RssFeed> CREATOR = new Parcelable.Creator<RssFeed>() {
		@Override
		public RssFeed createFromParcel(Parcel parcel) {
			return new RssFeed(parcel);
		}

		@Override
		public RssFeed[] newArray(int size) {
			return new RssFeed[size];
		}
	};
	
	/**
	 *  Constructor estándar.
	 */
	public RssFeed () {
		
		items = new ArrayList<RssItem>();
		description = ""; // evita punteros a nulo
		
	}
	
	/**
	 * Constructor serializado.
	 * Construye un RssFeed recibiendo los datos
	 * serializados en un objeto de la clase Parcel.
	 * @param parcel	Parcel que contiene los datos del feed.
	 */
	public RssFeed (Parcel parcel) {

		/** Alternativa:
		 * (Respetar el orden usado en el método writeToParcel)
		 * item = parcel.readString();
		 * ArrayList = parcel.createTypedArrayList(RssItem.CREATOR);
		 */

		Bundle data = parcel.readBundle();
		title = data.getString ("title");
		description = data.getString("description");
		
		//String stringLink = data.getString("link");
		//link = AuxFunctions.stringToURL(stringLink);
		
		link = (URL) data.getSerializable("link");
		
		items = data.getParcelableArrayList("items");

	}
	
	/**
	 * Escribe los datos de un RssFeed en un objeto de clase Parcel.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		Bundle data = new Bundle();
		data.putString("title", title);
		data.putString("description", description);

		//String stringLink = link.toString();
		//data.putString("link", stringLink);
		
		data.putSerializable("link", link);
		data.putParcelableArrayList("items", items);
		
		dest.writeBundle(data);
		
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	/**
	 *  Obtener título.
	 * @return	título del feed.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 *  Establecer título.
	 * @param title	título del feed.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 *  Obtener descripción.
	 * @return	descripción del feed.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 *  Establecer descripción.
	 * @param description	descripción del feed.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 *  Obtener link.
	 * @return	enlace del feed.
	 */
	public URL getLink() {
		return link;
	}
	
	/**
	 *  Establecer link desde URL.
	 * @param link	enlace del feed.
	 */
	public void setLink(URL link) {
		this.link = link;
	}
	
	/**
	 *  Establecer link desde String.
	 * @param link	enlace del feed.
	 */
	public void setLink(String link) {
		this.link = Utils.stringToURL(link);
	}
	
	/**
	 *  Añadir item recibiendo objeto RssItem.
	 * @param item	item que se añade.
	 */
	public void addItem (RssItem item) {
		items.add(item);
	}
	
	/**
	 *  Añadir item recibiendo datos del nuevo item
	 * @param title			título del feed que se añade.
	 * @param link			enlace del feed que se añade.
	 * @param description	descripción del feed que se añade.
	 * @param date			fecha de publicación del feed que se añade.
	 */
	public void addItem (String title, String link, String description, String date) {
		RssItem item = new RssItem();
		
		item.setTitle(title);
		item.setLink(link);
		item.setDescription(description);
		item.setPubDate(date);
		
		items.add(item);
		
	}
	
	/**
	 *  Obtener lista de items.
	 * @return	listado de items del feed.
	 */
	public ArrayList<RssItem> getItems() {
		return items;
	}
	
	/**
	 *  Obtener item por índice.
	 * @param index	índide del item que se desea obtener.
	 * @return		item obtenido.
	 */
	public RssItem getItem(int index) {
		if (items.size()>index) {
			return items.get(index);
		}
		
		// Se solicita un item que no existe
		Utils.MyLog ('d', "Se ha solicitado un RssItem que no existe");
		return null;
	}
	
	/**
	 *  Validar feed.
	 * @return	resultado de la operación.
	 */
	public boolean isValidFeed () {

		// Estrictamente se debería comprobar la descripción
		// pero hay demasiadas webs que prescinden de ella
		if (!this.title.equals("")) 
			if (this.link != null)
				return true;

		return false;
	}

}
