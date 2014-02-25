/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Mu�oz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.data;

import java.util.ArrayList;

/**
 * El panel es la estructura de las categor�as y sus suscripciones entorno
 * a la que gira el resto de la aplicaci�n.
 * 
 * Utiliza el patr�n DAO para separar los objetos de negocio del destino final
 * de la informaci�n que manipula.
 * Esto permite establecer y recuperar el panel mediante los m�todos definidos
 * en esta interfaz, intependientemente de la implementaci�n que se haga de
 * los mismos.
 * @author Manu
 * 
 * @see AlmacenStructXML_SAX.java
 *
 */
public interface AlmacenStructInterface {
	
	/**
	 *  Obtener panel de noticias.
	 * @return	listado de categor�as con sus feeds.
	 */
	public ArrayList<RssCategory> getFeedsStruct ();
	
	/**
	 *  Guardar panel de noticias.
	 * @param categories	listado de categor�as con sus feeds.
	 */
	public void setFeedsStruct (ArrayList<RssCategory> categories);

}
