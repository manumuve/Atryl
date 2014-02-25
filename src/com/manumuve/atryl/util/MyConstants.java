/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Mu�oz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.util;

import static java.util.Arrays.asList;

import java.util.ArrayList;

/**
 * Clase de constantes del programa.
 * Define las constantes y sus valores
 * 
 * @author Manumuve
 * @version 0.1
 */

public class MyConstants {

	/**
	 * Debug flag.
	 * True: la ejecuci�n loguea mensajes de depuraci�n.
	 * False: no se loguean mensajes de depuraci�n.
	 */
	public static final boolean DEBUG = false;

	/** M�todo de almacen para SAX parser */
	public static final int SAX_PARSER = 0;

	/**
	 * Nombre de la aplicaci�n. Se debe utilizar la definici�n de
	 * res/values/strings.xml
	 */
	public static final String APP_NAME = "Atryl";

	public static final int NO_DATA = -1;
	public static final int TOP_STORIES = -2;

	/** Valores de retorno para m�todos */
	public static final int OK = 0;
	public static final int KO = -1;

	/** Densidad de pantalla para tablets.
	 * @see Utils.isSmartphone
	 */
	public static final int TABLET_MIN_DP_WEIGHT = 450;

	/** Valores de retorno para m�todos de interfaces Java */
	public static final int NO_ERROR = 0;
	public static final int ERROR = -1;

	/** Formato de fecha personalizado */
	public static String DATE_FORMAT = "EEEE � dd MMMM yyyy � HH:mm";

	/**
	 * Etiquetas para las que se desea generar un evento NoSuchMethodException
	 * en la clase RssHandler
	 */
	public static final ArrayList<String> XML_PARSE_EXCEPTIONS = new ArrayList<String>(
			asList("setRss",
					"setChannel",
					"setItem",
					"setPubDate", // cuando salta en feed
					"setComments",
					"setCopyright",

					// Men�ame
					"setMeneameComments",
					"setMeneameKarma",
					"setMeneameLink_id",
					"setMeneameNegatives",
					"setMeneameStatus",
					"setMeneameUrl",
					"setMeneameUser",
					"setMeneameVotes",

					// Atom
					"setAtom10Link",
					"setAtomLink",

					"setWfwCommentRss",
					"setSlashComments",
					"setMediaThumbnail",
					"setLastBuildDate",
					"setTtl",
					"setGenerator",
					"setMediaDescription",
					"setUrl",
					"setSyUpdatePeriod",
					"setSyUpdateFrequency",
					"setSyUpdateBase",
					"setCreativeCommonsLicense",
					"setXhtmlMeta",

					// Feedburner
					"setFeedburnerBrowserFriendly",
					"setFeedburnerEmailServiceId",
					"setFeedburnerFeedburnerHostname",
					"setFeedburnerFeedFlare",
					"setFeedburnerInfo",
					"setFeedburnerOrigLink"));

}
