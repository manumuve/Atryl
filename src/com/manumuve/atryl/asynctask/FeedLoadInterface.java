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
    
    public void onFeedLoadProgress (String msg);
    
    public void onFeedLoadComplete(RssFeed rssFeed);
    
}
