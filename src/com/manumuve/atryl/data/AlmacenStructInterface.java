/**
 * El panel es la estructura de las categor�as y sus suscripciones entorno
 * a la que gira el resto de la aplicaci�n.
 * 
 * No se ha decidido un m�todo con el que almacenar de forma persistente el
 * panel, por lo que es necesario definir una interfaz que especifique los
 * m�todos necesarios y as� permitir cierta flexibilidad a la hora de utilizar
 * un m�todo concreto que los implemente.
 * @author Manu
 *
 */

package com.manumuve.atryl.data;

import java.util.ArrayList;

public interface AlmacenStructInterface {
	
	// Obtener panel de noticias
	public ArrayList<RssCategory> getFeedsStruct ();
	
	// Guardar panel de noticias
	public void setFeedsStruct (ArrayList<RssCategory> categories);

}
