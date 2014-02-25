/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import com.manumuve.atryl.util.Utils;

/**
 * Esta clase representa cada una de las noticias contenidas en una suscripción.
 * Contiene los atributos obligatorios: title, link y description, además de
 * una serie de atributos opcionales que son procesados en los métodos de la
 * clase.
 * @author Manu
 *
 */
public class RssItem implements Parcelable {
	
	/** RSS 2.0.11 Specification
	 * The current version of the RSS spec will always
	 * be available at this link
	 * http://www.rssboard.org/rss-specification
	 */
	private String title;					// The title of the item.
	private String link;					// The URL of the item.
	private String description; 			// The item synopsis.
	
	private String author; 					// Email address of the author of the item.	
	private ArrayList<String> category;		// Includes the item in one or more categories.
	private String comments;				// URL of a page for comments relating to the item.
	private String enclosure;				// Describes a media object that is attached to the item.
	private String guid;					// A string that uniquely identifies the item.
	private String stringPubDate;			// Indicates when the item was published.
	private Date datePubDate;				// Indicates when the item was published.
	private String source;					// The RSS channel that the item came from.
	/* END of RSS Specification */

	/** RSS extension tags and namespaces
	 * Different specifications to enhance RSS content
	 */
	
	/** Dublin Core Extending attributes
	 * http://purl.org/dc/elements/1.1/
	 */
	private String DcCreator; 				// An entity primarily responsible for making the resource.

	/** RDF Site Summary 1.0 Modules: Content attributes
	 * http://purl.org/rss/1.0/modules/content
	 */
	private String contentEncoded;			// An element whose contents are the entity-encoded or CDATA-escaped version of the content of the item.
	/* END of RSS extension hashtags and namespaces */

	
	public static final Parcelable.Creator<RssItem> CREATOR = new Parcelable.Creator<RssItem>() {
		@Override
		public RssItem createFromParcel(Parcel parcel) {
			return new RssItem(parcel);
		}

		@Override
		public RssItem[] newArray(int size) {
			return new RssItem[size];
		}
	};
	
	/**
	 * Constructor estándar.
	 */
	public RssItem () {
		
	}
	
	/**
	 * Constructor serializado.
	 * Construye un RssItem recibiendo los datos
	 * serializados en un objeto de la clase Parcel.
	 * @param parcel	Parcel que contiene los datos del feed.
	 */
	public RssItem (Parcel parcel) {
		
		/** Alternativa:
		 * (Respetar el orden usado en el método writeToParcel)
		 * item = parcel.readString();
		 * ArrayList = parcel.createTypedArrayList(RssItem.CREATOR);
		 */

		Bundle data = parcel.readBundle();
		title = data.getString ("title");
		description = data.getString("description");
		//link = data.getString("link");
		setLink (data.getString("link"));
		stringPubDate = data.getString("pubDate");
		contentEncoded = data.getString("content");
		//feed = data.getParcelable("feed");
		
	}
	
	/**
	 * Escribe los datos de un RssItem en un objeto de clase Parcel.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		Bundle data = new Bundle();
		data.putString("title", title);
		data.putString("link", link);
		data.putSerializable("pubDate", stringPubDate);
		data.putString("description", description);
		data.putString("content", contentEncoded);
		//data.putParcelable("feed", feed);
		
		dest.writeBundle(data);
		
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Obtener título.
	 * @return	título del item.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Establecer título.
	 * @param title	título del item.
	 */
	public void setTitle(String title) {
		this.title = Html.fromHtml(title).toString();
	}
	
	/**
	 * Obtener descripción.
	 * @return	descripción del item.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Establecer descripción.
	 * Establece la descripción del item. Se ha intentado dar formato a la descripción,
	 * en este método (ver código comentado) pero resulta un proceso muy pesado en
	 * dispositivos móviles.
	 * La descripción se guarda tal y como se recibe, y el formato se aplica solo
	 * en el momento de mostrar el item.
	 * @see FragmentFeedItem.java
	 * @param description	descripción del item.
	 */
	public void setDescription(String description) {
		/*
		String html = this.items.get(position).getDescription();

		html = html.replaceAll("<(.*?)\\>"," ");//Removes all items in brackets
		html = html.replaceAll("<(.*?)\\\n"," ");//Must be undeneath
		html = html.replaceFirst("(.*?)\\>", " ");//Removes any connected item to the last bracket
		html = html.replaceAll("&nbsp;"," ");
		html = html.replaceAll("&amp;"," ");

		o

		myHtmlString.replaceAll("s/<(.*?)>//g","");

		if (this.items.get(position).getDescription().length() > 0) {
			txDescription.setText(this.items.get(position).getDescription()
					.substring(0, 24)
					+ "...");
		}
		*/ 
		this.description = description;
	}
	
	/**
	 * Obtener enlace del item.
	 * Si existe guid y es una URL, se devuelve guid,
	 * si no se devuelve link.
	 * @return	enlace del item.
	 */
	public String getLink() {
		// Si guid es URL válida, devolver guid
		if (Utils.stringToURL(guid) != null) {
			return guid;
		}
		
		return link;
	}

	/**
	 * Establecer enlace.
	 * Se formatea el parámetro link antes de añadirlo como enlace
	 * del item.
	 * @param link	enlace del item.
	 */
	public void setLink(String link) {
		// Forzar añadido de http al inicio
		if (!link.startsWith("http://") && !link.startsWith("https://")){
			this.link = "http://" + link;
		} else {
			this.link = link;
		}
	}

	/**
	 * Establecer guid.
	 * @param guid	guid del item.
	 */
	public void setGuid (String guid) {
		this.guid = guid;
	}
	
	/**
	 * Obtener guid.
	 * @return	guid del item.
	 */
	public String getGuid () {
		return guid;
	}
	
	/**
	 * Obtener fecha de publicación del item.
	 * @return	fecha de publicación del item.
	 */
	public String getPubDate() {
		return stringPubDate;
	}
	
	/**
	 * Obtener fecha de publicación del item,
	 * como objeto Date.
	 * @return	fecha de pulicación del item.
	 */
	public Date getDatePubDate() {
		return datePubDate;
	}
	
	/**
	 * Establecer fecha de publicación del item.
	 * Se establece la fecha de publicación del item y
	 * una cadena de caracteres this.stringPubdate
	 * que mostrará la hora de publicación si el item se ha publicado
	 * hoy, "AYER" si se publicó ayer y la fecha si se publicó anteriormente.
	 * 
	 * @param pubDate	fecha de publicación del item.
	 */
	public void setPubDate(String pubDate) {
		DateFormat formatter = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
		try {
			Date date = formatter.parse(pubDate);
			this.datePubDate = date;
			
			Date now = new Date();
			int diffHours = (int) (now.getTime() - date.getTime()) / (1000*60*60);

			if (diffHours<=48 && now.getDay() == date.getDay()) {
				// hoy
				this.stringPubDate = new SimpleDateFormat("HH:mm",	Locale.getDefault()).format(date);
				
			} else if (diffHours<=48 && now.getDay() != date.getDay()) {
				// ayer
				this.stringPubDate = "AYER"; //TODO: string.xml
			} else {
				// fecha
				this.stringPubDate = new SimpleDateFormat("dd/MMMM/yyyy",	Locale.getDefault()).format(date);
			}
			
			//this.stringPubDate = new SimpleDateFormat(MyConstants.DATE_FORMAT,
			//		Locale.getDefault()).format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.stringPubDate = pubDate;
		}
	}
	
	/**
	 * Recuperar cuerpo del item.
	 * Si no se ha establecido el atributo contentEncoded,
	 * se devuelve la descripción.
	 * @return	cuerpo del item.
	 */
	public String getContentEncoded() {
		
		if (contentEncoded == null) {
			return description;
		}
		
		return contentEncoded;
	}

	/**
	 * Establecer cuerpo del item.
	 * @param content	cuerpo del item.
	 */
	public void setContentEncoded(String content) {
		this.contentEncoded = content;
	}
	
	/**
	 * Obtener autor del item.
	 * @return	autor del item.
	 */
	public String getDcCreator() {
		return DcCreator;
	}

	/**
	 * Establecer autor del item.
	 * @param dcCreator	autor del item.
	 */
	public void setDcCreator(String dcCreator) {
		this.DcCreator = dcCreator;
	}
	
	/**
	 * Establecer categoría del item.
	 * @param category	categoría del item.
	 */
	public void setCategory (String category) {
		this.category.add(category);
	}
	
	/**
	 * Obtener categorías del item.
	 * @return	lista de categorías del item.
	 */
	public ArrayList<String> getCategory () {
		return category;
	}
	
	/**
	 * Obtener autor del item.
	 * Si no se ha establecido el atributo DcCreator,
	 * se devuelve el atributo author.
	 * @return	autor del item.
	 */
	public String getAuthor() {
		if (DcCreator != null) {
			return DcCreator;
		}
		
		return author;
	}

	/**
	 * Establecer autor del item.
	 * @param author	autor del item.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

}
