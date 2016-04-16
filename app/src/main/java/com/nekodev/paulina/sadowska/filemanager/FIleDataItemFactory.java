package com.nekodev.paulina.sadowska.filemanager;

import android.app.Activity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Paulina Sadowska on 16.04.16.
 */
public class FileDataItemFactory {

    private Activity mActivity;

    public FileDataItemFactory(Activity mActivity){
        this.mActivity = mActivity;
    }

    private static final ArrayList<String> sizeUnits;
    static
    {
        sizeUnits = new ArrayList<>();
        sizeUnits.add("B");
        sizeUnits.add("kB");
        sizeUnits.add("MB");
        sizeUnits.add("GB");
    }

    public FileDataItem createFileDataItem(File file){

        FileDataItem fileItem = new FileDataItem();
        fileItem.setName(file.getName());

        if(file.isFile())
        {
            fileItem.setType(FileType.FILE);
            fileItem.setSize(fileSizeToString(file.length()));
        }
        else if(file.isDirectory())
        {
            String size= "?";
            fileItem.setType(FileType.DIRECTORY);
            if(file.list()!=null)
                size = directorySizeToString(file.list().length);
            else if(file.canRead())
                size = directorySizeToString(0);
            fileItem.setSize(size);
        }
        else
            fileItem.setType(FileType.UNKNOWN);

        CustomDateFormat lastModifiedDate = new CustomDateFormat(new Date(file.lastModified()), mActivity);
        fileItem.setLastModified(lastModifiedDate.toString());
        fileItem.setReadable(file.canRead());
        fileItem.setAbsolutePath(file.getAbsolutePath());
        return fileItem;
    }

    private String fileSizeToString(long fileSize){

        int i=0;
        double tempSize = fileSize;
        while((tempSize/1024 >= 1) && i<sizeUnits.size()){
            tempSize/=1024;
            i++;
        }
        return new DecimalFormat("#0.0").format(tempSize) + sizeUnits.get(i);
    }

    private String directorySizeToString(int numOfFiles){

        return numOfFiles + "";
    }
}
