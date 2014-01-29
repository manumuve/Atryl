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
    
    public void onXmlLoadProgress (int category, int feed,int error);
    
    public void onXmlLoadComplete(int category, int feed,int error);
    
}
