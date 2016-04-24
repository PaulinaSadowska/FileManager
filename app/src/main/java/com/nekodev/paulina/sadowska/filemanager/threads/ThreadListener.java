package com.nekodev.paulina.sadowska.filemanager.threads;

/**
 * Created by Paulina Sadowska on 21.04.16.
 */
public interface ThreadListener {
    void notifyOfThreadComplete(final Runnable runnable);
    void notifyProgressChanged(int progress);
}
