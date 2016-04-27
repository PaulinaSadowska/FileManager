package com.nekodev.paulina.sadowska.filemanager.data;

import android.graphics.Bitmap;

/**
 * Created by Paulina Sadowska on 27.04.16.
 */
public class IndexedBitmap{
    private Bitmap image;
    private int index;

    public IndexedBitmap(Bitmap image, int index){
        this.image = image;
        this.index = index;
    }

    public Bitmap getImage(){
        return image;
    }

    public int getIndex(){
        return index;
    }
}
