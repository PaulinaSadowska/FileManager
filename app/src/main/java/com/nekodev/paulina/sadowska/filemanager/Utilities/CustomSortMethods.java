package com.nekodev.paulina.sadowska.filemanager.utilities;

import com.nekodev.paulina.sadowska.filemanager.data.FileDataItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Paulina Sadowska on 23.04.16.
 */
public class CustomSortMethods {

    public static List<FileDataItem> sortByName(List<FileDataItem> fileDataList, final int AscOrDesc) {
        Collections.sort(fileDataList, new Comparator<FileDataItem>() {
            @Override
            public int compare(final FileDataItem file1, final FileDataItem file2) {
                return file1.getName().compareTo(file2.getName()) * getDirection(AscOrDesc);
            }
        });
        return fileDataList;
    }

    public static List<FileDataItem> sortByDate(List<FileDataItem> fileDataList, final int AscOrDesc) {
        Collections.sort(fileDataList, new Comparator<FileDataItem>() {
            @Override
            public int compare(final FileDataItem file1, final FileDataItem file2) {
                return (file1.getLastModified().compareTo(file2.getLastModified()))* getDirection(AscOrDesc);
            }
        });
        return fileDataList;
    }

    public static  List<FileDataItem> sortBySize(List<FileDataItem> fileDataList, final int AscOrDesc) {
        Collections.sort(fileDataList, new Comparator<FileDataItem>() {
            @Override
            public int compare(final FileDataItem file1, final FileDataItem file2) {
                return (file1.getSize().compareTo(file2.getSize()))* getDirection(AscOrDesc);
            }
        });
        return fileDataList;
    }

    private static int getDirection(int ascOrDesc){
        if(ascOrDesc==Constants.SORTING_DIRECTION.DESCENDING)
            return  -1;
        return 1;
    }

}
