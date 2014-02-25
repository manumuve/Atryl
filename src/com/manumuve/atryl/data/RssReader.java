/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.data;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * Esta clase tendrá únicamente un constructor que reciba como parámetro
 * la URL del documento a parsear, y un método público llamado parse()
 * para ejecutar la lectura del documento, y que devolverá como resultado
 * una lista de noticias.
 * @author Manu
 *
 */

public class RssReader {

	/**
	 * Recibe una URL y devuelve el objeto RssFeed correspondiente, en caso
	 * de existir.
	 * @param url
	 * @return RssFeed leído en la URL recibida.
	 * @throws SAXException
	 * @throws IOException
	 */
	public static RssFeed read(URL url) throws SAXException, IOException {
		
		// TODO: está bien comprobar esto, pero jamás debería producirse
		if (url == null)
			return null;
		
		try {
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			RssHandler handler = new RssHandler();
			
			InputSource input = new InputSource(url.openStream());
			
			reader.setContentHandler(handler);
			reader.parse(input);
			
			RssFeed result = handler.getResult();
			if (result != null) {
				result.setLink(url); // TODO: mejorar la forma de establecer el enlace del feed
			}
			
			return result;
			
		} catch (ParserConfigurationException e) {
			
			throw new SAXException();
		
		} catch (ProtocolException e) {
			return null;
		}

	}
}
