package com.manumuve.atryl.util;

import static java.util.Arrays.asList;

import java.util.ArrayList;

/** Clase de constantes del programa
* Define las constantes y sus valores
* @author Manumuve
* @version 0.1
*/

public class MyConstants {
	
	/** Debug flag
	 * True: la ejecución loguea mensajes de depuración
	 * False: no se loguean mensajes de depuración
	 */
	public static final boolean DEBUG = false;
	
	/** Método de almacen */
	public static final int SAX_PARSER = 0;
	
	 // TODO: debería estar exclusivamente en strings.xml
	public static final String APP_NAME = "Atryl";
	
    public static final int NO_DATA = -1;
    public static final int TOP_STORIES = -2;
    
    // Retornos de ejecución
    public static final int OK = 0;
    public static final int KO = -1;
    
    
    // Utils.isSmartphone
    public static final int TABLET_MIN_DP_WEIGHT = 450;
    
    //XmlLoadInterface
    public static final int NO_ERROR = 0;
    public static final int ERROR = -1;
    
    // Formato de fecha
    public static String DATE_FORMAT = "EEEE · dd MMMM yyyy · HH:mm";
    
    /* Etiquetas para las que no quiero generar un evento
     * NoSuchMethodException en RssHandler
     */
    public static final ArrayList<String> XML_PARSE_EXCEPTIONS = new ArrayList<String>(asList(
    	"setRss",
    	"setChannel",
    	"setItem",
    	"setPubDate", // cuando salta en feed
    	"setComments",
    	"setCopyright",

    	"setMeneameComments",
    	"setMeneameKarma",
    	"setMeneameLink_id",
    	"setMeneameNegatives",
    	"setMeneameStatus",
    	"setMeneameUrl",
    	"setMeneameUser",
    	"setMeneameVotes",
    	
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
    	
    	"setFeedburnerBrowserFriendly",
    	"setFeedburnerEmailServiceId",
    	"setFeedburnerFeedburnerHostname",
    	"setFeedburnerFeedFlare",
    	"setFeedburnerInfo",
    	"setFeedburnerOrigLink"
    ));
    
}