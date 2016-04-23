package com.nekodev.paulina.sadowska.filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Paulina Sadowska on 21.04.16.
 */
public class FileUtils {

    public static ArrayList<File> getListOfFiles(String path) {
        File dir = new File(path);
        String[] list = dir.list();
        ArrayList<File> fileList = new ArrayList<>();
        if (list != null) {
            for (String fileName : list) {
                if (!fileName.startsWith(".")) {
                    fileList.add(new File(FileUtils.getFullFileName(path, fileName)));
                }
            }
        }
        return fileList;
    }

    public static FileType getFileType(File file) {
        if(!file.canRead())
            return FileType.UNKNOWN;
        if(file.isFile())
            return FileType.FILE;
        if(file.isDirectory())
            return FileType.DIRECTORY;
        return FileType.UNKNOWN;
    }

    public static String getFullFileName(String path, String fileName) {
        if (path.endsWith(File.separator)) {
            return (path + fileName);
        } else {
            return (path + File.separator + fileName);
        }
    }

    public static ArrayList<FileDataItem> sortByName(ArrayList<FileDataItem> fileDataList) {
        Collections.sort(fileDataList, new Comparator<FileDataItem>() {
            @Override
            public int compare(final FileDataItem file1, final FileDataItem file2) {
                return file1.getName().compareTo(file2.getName());
            }
        });
        return fileDataList;
    }

    public static  ArrayList<FileDataItem> sortByDate(ArrayList<FileDataItem> fileDataList) {
        Collections.sort(fileDataList, new Comparator<FileDataItem>() {
            @Override
            public int compare(final FileDataItem file1, final FileDataItem file2) {
                return file1.getLastModified().compareTo(file2.getLastModified());
            }
        });
        return fileDataList;
    }

    public static  ArrayList<FileDataItem> sortBySize(ArrayList<FileDataItem> fileDataList) {
        Collections.sort(fileDataList, new Comparator<FileDataItem>() {
            @Override
            public int compare(final FileDataItem file1, final FileDataItem file2) {
                return file1.getSize().compareTo(file2.getSize());
            }
        });
        return fileDataList;
    }

}
