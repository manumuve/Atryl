/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.data;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.manumuve.atryl.util.Utils;

/**
 * Clase que guarda la estructura de categorías y suscripciones del
 * usuario en un fichero XML.
 * Implementa la interfaz AlmacenStructInterface.
 * @author Manu
 *
 */
public class AlmacenStructXML_SAX implements AlmacenStructInterface {
	private static String FICHERO = "listacategorias.xml"; // fichero donde se guarda el panel
	public ArrayList<RssCategory> categories; // lista de categorias
	private Context context;
	private boolean _isLoaded;

	/**
	 *  Constructor
	 * @param context
	 */
	public AlmacenStructXML_SAX(Context context) {
		this.context = context;
		categories = new ArrayList<RssCategory>();
		_isLoaded = false;
	}

	/**
	 * Obtener listado de categorías con sus feeds.
	 */
	@Override
	public ArrayList<RssCategory> getFeedsStruct() {
		
		if (!_isLoaded) {
			try {
				readXML(context.openFileInput(FICHERO));
			} catch (FileNotFoundException e) { // no existe el fichero
				Utils.MyLog('d', "No existe el fichero");
			} catch (Exception e) {
				Log.e("Atryl", e.getMessage(), e); // otro tipo de problema
			}
		}
		
		return categories;
	}

	/**
	 * Establecer listado de categorías con sus feeds.
	 */
	@Override
	public void setFeedsStruct(ArrayList<RssCategory> categories) {
		this.categories = categories;
		try {
			writeXML(context.openFileOutput(FICHERO, Context.MODE_PRIVATE));
		} catch (Exception e) {
			Log.e("Atryl", e.getMessage(), e); // problema escribiendo fichero
		}
	}

	/**
	 * Leer datos de fichero XML.
	 * @param inputStream	fichero XML que se lee.
	 * @throws Exception
	 */
	public void readXML(InputStream inputStream) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		HandlerXML handlerXML = new HandlerXML();
		reader.setContentHandler(handlerXML);
		reader.parse(new InputSource(inputStream));
		_isLoaded = true;
	}

	/**
	 * Almacenar datos en fichero XML.
	 * @param outputStream	fichero donde se escribe.
	 */
	public void writeXML(OutputStream outputStream) {
		int i, j; // category counter
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(outputStream, "UTF-8");
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "category_list");
			
			for (i=0; i<categories.size(); i++) {
			//for (RssCategory category : categories) { // for each
				serializer.startTag("", "category");
				serializer.attribute("", "name", String.valueOf(categories.get(i).getName()));
				for (j=0; j<categories.get(i).getFeeds().size(); j++) {
					serializer.startTag("", "feed");
					serializer.attribute("", "title", categories.get(i).getFeed(j).getTitle());
					serializer.startTag("", "link");
					serializer.text(categories.get(i).getFeed(j).getLink().toString());
					serializer.endTag ("", "link");
					serializer.startTag("", "description");
					serializer.text(categories.get(i).getFeed(j).getDescription());
					serializer.endTag ("", "description");
					serializer.endTag("", "feed");
				}
				serializer.endTag("", "category");
			}
			serializer.endTag("", "category_list");
			serializer.endDocument();
		} catch (Exception e) {
			Log.e("Atryl", e.getMessage(), e);
		}
	}

	/**
	 * Clase manejadora del fichero xml donde se almacena
	 * la estructura de categorías con sus feeds.
	 * @author Manu
	 *
	 */
	class HandlerXML extends DefaultHandler {
		private String chars;
		private RssCategory category;
		private RssFeed feed;
		private boolean _inCategory, _inFeed, _inLink, _inDescription;

		/**
		 * This gets called when the xml document is first opened
		 * 
		 * @throws SAXException
		 */
		@Override
		public void startDocument() throws SAXException {
			// no hace falta hacer nada, la lista de categorias esta iniciada en
			// el constructor de la superclase
		}

		/**
		 * This gets called at the start of an element. Here we're also setting
		 * the booleans to true if it's at that specific tag. (so we know where
		 * we are)
		 * 
		 * @param namespaceURI
		 * @param localName
		 * @param qName
		 * @param atts
		 * @throws SAXException
		 */
		@Override
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {

			if (localName.equals("category")) {
				_inCategory = true;
				category = new RssCategory();
				category.setName(atts.getValue("name"));
				//Utils.MyLog('d', "<category name="+atts.getValue("name")+">");
			}
			if (localName.equals("feed")) {
				_inFeed = true;
				feed = new RssFeed();
				feed.setTitle(atts.getValue("title"));
				//Utils.MyLog('d', "<feed title="+atts.getValue("title")+">");
			}
			if (localName.equals("link")) {
				_inLink = true;
			}
			if (localName.equals("description")) {
				_inDescription = true;
			}
		}

		/**
		 * Called at the end of the element. Setting the booleans to false, so
		 * we know that we've just left that tag.
		 * 
		 * @param namespaceURI
		 * @param localName
		 * @param qName
		 * @throws SAXException
		 */
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (localName.equals("category")) {
				_inCategory = false;
				categories.add(category);
			}
			if (localName.equals("feed")) {
				_inFeed = false;
				category.addFeed(feed);
			}
			if (localName.equals("link")) {
				_inLink = false;
			}
			if (localName.equals("description")) {
				_inDescription = false;
			}
		}

		/**
		 * Called when it's finished handling the document
		 * 
		 * @throws SAXException
		 */
		@Override
		public void endDocument() throws SAXException {
		}

		/**
		 * Calling when we're within an element. Here we're checking to see if
		 * there is any content in the tags that we're interested in and
		 * populating it in the Config object.
		 * 
		 * @param ch
		 * @param start
		 * @param length
		 */
		@Override
		public void characters(char ch[], int start, int length) {
			chars = new String(ch, start, length);
			chars = chars.trim();

			if (_inCategory) {
				if (_inFeed) {
					if (_inLink) {
						feed.setLink(Utils.stringToURL(chars));
						//Utils.MyLog('d', "<link>"+chars+"</link>");
					}
					if (_inDescription) {
						feed.setDescription(chars);
						//Utils.MyLog('d', "<feed title="+chars+"</description>");
					}
				}
			}
		}
	}

}
