package com.nekodev.paulina.sadowska.filemanager;

/**
 * Created by Paulina Sadowska on 21.04.16.
 */
public interface ThreadCompleteListener {
    void notifyOfThreadComplete(final Runnable runnable);
}
