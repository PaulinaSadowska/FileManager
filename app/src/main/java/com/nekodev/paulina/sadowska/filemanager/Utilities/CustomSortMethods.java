package com.nekodev.paulina.sadowska.filemanager.utilities;

import com.nekodev.paulina.sadowska.filemanager.data.FileDataItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Paulina Sadowska on 23.04.16.
 */
public class CustomSortMethods {

    public static ArrayList<FileDataItem> sortByName(ArrayList<FileDataItem> fileDataList, final int AscOrDesc) {
        Collections.sort(fileDataList, new Comparator<FileDataItem>() {
            @Override
            public int compare(final FileDataItem file1, final FileDataItem file2) {
                return file1.getName().compareTo(file2.getName()) * AscOrDesc;
            }
        });
        return fileDataList;
    }

    public static  ArrayList<FileDataItem> sortByDate(ArrayList<FileDataItem> fileDataList, final int AscOrDesc) {
        Collections.sort(fileDataList, new Comparator<FileDataItem>() {
            @Override
            public int compare(final FileDataItem file1, final FileDataItem file2) {
                return (file1.getLastModified().compareTo(file2.getLastModified()))*AscOrDesc;
            }
        });
        return fileDataList;
    }

    public static  ArrayList<FileDataItem> sortBySize(ArrayList<FileDataItem> fileDataList, final int AscOrDesc) {
        Collections.sort(fileDataList, new Comparator<FileDataItem>() {
            @Override
            public int compare(final FileDataItem file1, final FileDataItem file2) {
                return (file1.getSize().compareTo(file2.getSize()))*AscOrDesc;
            }
        });
        return fileDataList;
    }

}
