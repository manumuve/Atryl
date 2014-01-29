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
	
	public RssItem () {
		
	}
	
	public RssItem (Parcel parcel) {
		
		/* Alternativa:
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
//		feed = data.getParcelable("feed");
		
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		Bundle data = new Bundle();
		data.putString("title", title);
		data.putString("link", link);
		data.putSerializable("pubDate", stringPubDate);
		data.putString("description", description);
		data.putString("content", contentEncoded);
//		data.putParcelable("feed", feed);
		
		dest.writeBundle(data);
		
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = Html.fromHtml(title).toString();
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
//      String html = this.items.get(position).getDescription();
//      
//    html = html.replaceAll("<(.*?)\\>"," ");//Removes all items in brackets
//    html = html.replaceAll("<(.*?)\\\n"," ");//Must be undeneath
//    html = html.replaceFirst("(.*?)\\>", " ");//Removes any connected item to the last bracket
//    html = html.replaceAll("&nbsp;"," ");
//    html = html.replaceAll("&amp;"," ");
//      
//    o
//    
//    myHtmlString.replaceAll("s/<(.*?)>//g","");
      
//      if (this.items.get(position).getDescription().length() > 0) {
//          txDescription.setText(this.items.get(position).getDescription()
//          		.substring(0, 24)
//          		+ "...");
//      } 
		this.description = description;
	}
	
	public String getLink() {
		// Si guid es URL válida, devolver guid
		if (Utils.stringToURL(guid) != null) {
			return guid;
		}
		
		return link;
	}

	public void setLink(String link) {
		// Forzar añadido de http al inicio
		if (!link.startsWith("http://") && !link.startsWith("https://")){
			this.link = "http://" + link;
		} else {
			this.link = link;
		}
	}

	public void setGuid (String guid) {
		this.guid = guid;
	}
	
	public String getGuid () {
		return guid;
	}
	
	public String getPubDate() {
		return stringPubDate;
	}
	
	public Date getDatePubDate() {
		return datePubDate;
	}
	
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
				this.stringPubDate = "AYER";
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
	
	public String getContentEncoded() {
		
		if (contentEncoded == null) {
			return description;
		}
		
		return contentEncoded;
	}

	public void setContentEncoded(String content) {
		this.contentEncoded = content;
	}
	
	public String getDcCreator() {
		return DcCreator;
	}

	public void setDcCreator(String dcCreator) {
		this.DcCreator = dcCreator;
	}
	
	public void setCategory (String category) {
		this.category.add(category);
	}
	
	public ArrayList<String> getCategory () {
		return category;
	}
	
	public String getAuthor() {
		if (DcCreator != null) {
			return DcCreator;
		}
		
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
