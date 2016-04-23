package com.nekodev.paulina.sadowska.filemanager.data.factories;

import android.app.Activity;

import com.nekodev.paulina.sadowska.filemanager.data.CustomSize;
import com.nekodev.paulina.sadowska.filemanager.data.FileDataItem;
import com.nekodev.paulina.sadowska.filemanager.data.FileType;

import java.io.File;
import java.util.Date;

/**
 * Created by Paulina Sadowska on 16.04.16.
 */
public class FileDataItemFactory {

    private Activity mActivity;

    public FileDataItemFactory(Activity mActivity){
        this.mActivity = mActivity;
    }

    public FileDataItem createFileDataItem(File file){

        FileDataItem fileItem = new FileDataItem();
        fileItem.setName(file.getName());

        if(file.isFile())
        {
            fileItem.setType(FileType.FILE);
            fileItem.setSize(new CustomSize(file.length(), false));
        }
        else if(file.isDirectory())
        {
            long size= -1;
            fileItem.setType(FileType.DIRECTORY);
            if(file.list()!=null)
                size = file.list().length;
            else if(file.canRead())
                size = 0;
            fileItem.setSize(new CustomSize(size, true));
        }
        else{
            fileItem.setType(FileType.UNKNOWN);
            fileItem.setSize(new CustomSize());
        }

        CustomDateFactory lastModifiedDate = new CustomDateFactory(new Date(file.lastModified()), mActivity);
        fileItem.setLastModified(lastModifiedDate.build());
        fileItem.setReadable(file.canRead());
        fileItem.setAbsolutePath(file.getAbsolutePath());
        return fileItem;
    }
}
