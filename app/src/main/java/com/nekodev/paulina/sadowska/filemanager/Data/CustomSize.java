package com.nekodev.paulina.sadowska.filemanager.data;

import com.nekodev.paulina.sadowska.filemanager.utilities.Constants;

import java.text.DecimalFormat;

/**
 * Created by Paulina Sadowska on 23.04.16.
 */
public class CustomSize {

    private Double size;
    private Long sizeInBytes;
    private String unit;
    private boolean isDirectory;


    public CustomSize() {
        size = -1.0;
        sizeInBytes = (long) -1;
        isDirectory = false;
        unit = "";
    }

    public CustomSize(Long size, boolean isDirectory) {

        this.isDirectory = isDirectory;
        this.sizeInBytes = size;
        if (isDirectory) {
            this.size = size * 1.0;
            this.unit = "";
        } else {
            int i = 0;
            double tempSize = size;
            while ((tempSize / 1024 >= 1) && i < Constants.sizeUnits.size()) {
                tempSize /= 1024;
                i++;
            }
            this.size = tempSize;
            unit = Constants.sizeUnits.get(i);
        }
    }


    public String toString() {
        if(size==null){
            return "?";
        }
        if (size<0) {
            return "?";
        }

        if (isDirectory)
            return new DecimalFormat("#0").format(size);

        return new DecimalFormat("#0.0").format(size) + unit;

    }

    public Double getSizeValue() {
        return size;
    }

    public Long getSizeInBytesValue() {
        return sizeInBytes;
    }

    public int compareTo(CustomSize sizeToCompare) {

        if(this.size<0 && sizeToCompare.getSizeValue()>=0)
            return 1;
        if(sizeToCompare.getSizeValue()<0 && this.size>=0)
            return -1;

        if (this.isDirectory && sizeToCompare.isDirectory()) {
            return this.size.compareTo(sizeToCompare.getSizeValue());
        } else if (this.isDirectory) {
            return -1;
        } else if (sizeToCompare.isDirectory()) {
            return 1;
        } else {
            return this.sizeInBytes.compareTo(sizeToCompare.getSizeInBytesValue());
        }
    }

    public boolean isDirectory() {
        return this.isDirectory;
    }
}
