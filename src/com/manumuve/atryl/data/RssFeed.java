package com.manumuve.atryl.data;

import java.net.URL;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.manumuve.atryl.util.Utils;


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
	
	// Constructor
	public RssFeed () {
		
		items = new ArrayList<RssItem>();
		description = ""; // evita punteros a nulo
		
	}
	
	public RssFeed (Parcel parcel) {

		/* Alternativa:
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
	
	// Obtener título
	public String getTitle() {
		return title;
	}
	
	// Establecer título
	public void setTitle(String title) {
		this.title = title;
	}
	
	// Obtener descripción
	public String getDescription() {
		return description;
	}
	
	// Establecer descripción
	public void setDescription(String description) {
		this.description = description;
	}
	
	// Obtener link
	public URL getLink() {
		return link;
	}
	
	// Establecer link
	public void setLink(URL link) {
		this.link = link;
	}
	
	// Establecer link
	public void setLink(String link) {
		this.link = Utils.stringToURL(link);
	}
	
	// Añadir item recibiendo objeto item
	public void addItem (RssItem item) {
		items.add(item);
	}
	
	// Añadir item recibiendo datos del nuevo item
	public void addItem (String title, String link, String description, String date) {
		RssItem item = new RssItem();
		
		item.setTitle(title);
		item.setLink(link);
		item.setDescription(description);
		item.setPubDate(date);
		
		items.add(item);
		
	}
	
	// Obtener lista de items
	public ArrayList<RssItem> getItems() {
		return items;
	}
	
	// Obtener item por índice
	public RssItem getItem(int index) {
		if (items.size()>index) {
			return items.get(index);
		}
		
		// Se solicita un item que no existe
		Utils.MyLog ('d', "Se ha solicitado un RssItem que no existe");
		return null;
	}
	
	// Validar feed
	public boolean isValidFeed () {

		// Estrictamente se debería comprobar la descripción
		// pero hay demasiadas webs que prescinden de ella
		if (!this.title.equals("")) 
			if (this.link != null)
				return true;

		return false;
	}

}
