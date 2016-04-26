package com.nekodev.paulina.sadowska.filemanager.utilities;

import com.nekodev.paulina.sadowska.filemanager.data.FileType;

import java.io.File;
import java.util.ArrayList;

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

    public static ArrayList<String> getListOfFileNames(String path) {
        File dir = new File(path);
        String[] list = dir.list();
        ArrayList<String> fileList = new ArrayList<>();
        if (list != null) {
            for (String fileName : list) {
                if (!fileName.startsWith(".")) {
                    fileList.add(fileName);
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

    public static FileType getFileType(String type) {
        if(type.equals(FileType.FILE.toString()))
            return FileType.FILE;
        if(type.equals(FileType.DIRECTORY.toString()))
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

    public static String getFileExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        String extension = "";
        if (pos > 0) {
            extension = fileName.substring(pos + 1, fileName.length());
        }
        return extension;
    }

}
