/**
 * El panel es la estructura de las categorías y sus suscripciones entorno
 * a la que gira el resto de la aplicación.
 * 
 * No se ha decidido un método con el que almacenar de forma persistente el
 * panel, por lo que es necesario definir una interfaz que especifique los
 * métodos necesarios y así permitir cierta flexibilidad a la hora de utilizar
 * un método concreto que los implemente.
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
