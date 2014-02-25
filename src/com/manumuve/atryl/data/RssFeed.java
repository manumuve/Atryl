/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Mu�oz <manumuve@gmail.com>
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
 * a�ade en el sistema.
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
	 *  Constructor est�ndar.
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
		 * (Respetar el orden usado en el m�todo writeToParcel)
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
	 *  Obtener t�tulo.
	 * @return	t�tulo del feed.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 *  Establecer t�tulo.
	 * @param title	t�tulo del feed.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 *  Obtener descripci�n.
	 * @return	descripci�n del feed.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 *  Establecer descripci�n.
	 * @param description	descripci�n del feed.
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
	 *  A�adir item recibiendo objeto RssItem.
	 * @param item	item que se a�ade.
	 */
	public void addItem (RssItem item) {
		items.add(item);
	}
	
	/**
	 *  A�adir item recibiendo datos del nuevo item
	 * @param title			t�tulo del feed que se a�ade.
	 * @param link			enlace del feed que se a�ade.
	 * @param description	descripci�n del feed que se a�ade.
	 * @param date			fecha de publicaci�n del feed que se a�ade.
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
	 *  Obtener item por �ndice.
	 * @param index	�ndide del item que se desea obtener.
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
	 * @return	resultado de la operaci�n.
	 */
	public boolean isValidFeed () {

		// Estrictamente se deber�a comprobar la descripci�n
		// pero hay demasiadas webs que prescinden de ella
		if (!this.title.equals("")) 
			if (this.link != null)
				return true;

		return false;
	}

}
