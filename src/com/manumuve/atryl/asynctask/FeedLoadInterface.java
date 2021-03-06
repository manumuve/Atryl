/*******************************************************************************
 * Atryl: RSS news reader for Android Devices - v0.4 - 25/02/2014
 * https://github.com/manumuve/Atryl
 *
 * Copyright (c) 2014 "Manumuve" Manuel E Mu�oz <manumuve@gmail.com>
 * Dual licensed under the MIT and GPL licenses.
 *
 ******************************************************************************/
package com.manumuve.atryl.asynctask;

import com.manumuve.atryl.data.RssFeed;

/**
 * This is a useful callback mechanism so we can abstract our AsyncTasks out into separate, re-usable
 * and testable classes yet still retain a hook back into the calling activity. Basically, it'll make classes
 * cleaner and easier to unit test.
 *
 * @param <T>
 */
public interface FeedLoadInterface
{
    /**
     * Invoked when the AsyncTask has completed its execution.
     * @param result The resulting object from the AsyncTask.
     */
    public void onFeedLoadStart (String msg);
    
    /**
     * Llamada cuando se notifica el progreso de la operaci�n.
     * @param msg Mensaje notificando el progreso
     */
    public void onFeedLoadProgress (String msg);
    
    /**
     * Llamada cuando termina la operaci�n, notifica el final y devuelve el resultado.
     * @param rssFeed The resulting object from the AsyncTask.
     */
    public void onFeedLoadComplete(RssFeed rssFeed);
    
}
