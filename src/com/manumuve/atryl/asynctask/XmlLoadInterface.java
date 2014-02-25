/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Muñoz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.asynctask;

/**
 * This is a useful callback mechanism so we can abstract our AsyncTasks out into separate, re-usable
 * and testable classes yet still retain a hook back into the calling activity. Basically, it'll make classes
 * cleaner and easier to unit test.
 *
 * @param <T>
 */
public interface XmlLoadInterface
{
    /**
     * Invoked when the AsyncTask has completed its execution.
     * @param result The resulting object from the AsyncTask.
     */
    public void onXmlLoadStart (int category, int feed, int error);
    
    /**
     * Llamada cuando se notifica el progreso de la operación.
     * @param msg Mensaje notificando el progreso
     */
    public void onXmlLoadProgress (int category, int feed,int error);
    
    /**
     * Llamada cuando termina la operación, notifica el final y devuelve el resultado.
     * @param rssFeed The resulting object from the AsyncTask.
     */
    public void onXmlLoadComplete(int category, int feed,int error);
    
}
