package com.nekodev.paulina.sadowska.filemanager;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Paulina Sadowska on 10.04.16.
 */
public class FileDataItem {

    private String name;
    private FileType type;
    private String lastModified;
    private String size;
    private boolean readable;
    private String absolutePath;

    private static final ArrayList<String> sizeUnits;
    static
    {
        sizeUnits = new ArrayList<>();
        sizeUnits.add("B");
        sizeUnits.add("kB");
        sizeUnits.add("MB");
        sizeUnits.add("GB");
    }

    public FileDataItem(File file){
        name = file.getName();

        if(file.isFile())
        {
            type = FileType.FILE;
            size = fileSizeToString(file.length());
        }
        else if(file.isDirectory())
        {
            type = FileType.DIRECTORY;
            if(file.list()!=null)
                size = directorySizeToString(file.list().length);
            else if(file.canRead())
                size = directorySizeToString(0);
            else
                size = "?";
        }
        else
            type = FileType.UNKNOWN;

        Date lastModifiedDate = new Date(file.lastModified());
        lastModified = lastModifiedDate.toString();

        readable = file.canRead();
        absolutePath = file.getAbsolutePath();
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

    public String getName() {
        return name;
    }

    public FileType getType() {
        return type;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getSize() {
        return size;
    }

    public boolean isReadable() {
        return readable;
    }


    public String getAbsolutePath() {
        return absolutePath;
    }

}
