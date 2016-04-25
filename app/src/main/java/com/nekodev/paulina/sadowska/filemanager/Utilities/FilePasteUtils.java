package com.nekodev.paulina.sadowska.filemanager.utilities;

import android.util.Log;

import com.nekodev.paulina.sadowska.filemanager.data.FileType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Paulina Sadowska on 24.04.16.
 */
public class FilePasteUtils {


    public static boolean pasteWithChildren(String basePath, String destinationPath, String fileName, String newFileName, FileType fileType, boolean copy) {
        if (fileType == FileType.FILE) {
            return FilePasteUtils.pasteFile(basePath, destinationPath, fileName, newFileName, copy);
        }
        if (fileType == FileType.DIRECTORY) {
            return pasteDirectory(basePath, destinationPath, fileName, newFileName, copy);
        }
        return false; //unknown type or cannot read
    }

    private static boolean pasteDirectory(String basePath, String destinationPath, String directoryName, String newFileName, boolean copy) {

        ArrayList<File> fileList = FileUtils.getListOfFiles(FileUtils.getFullFileName(basePath, directoryName));
        boolean result = true;
        File dir = new File(FileUtils.getFullFileName(destinationPath, newFileName));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (File file : fileList) {
            result = (result && pasteWithChildren(file.getParent(), dir.getAbsolutePath(), file.getName(), file.getName(), FileUtils.getFileType(file), copy));
        }
        if (!copy) {
            new File(FileUtils.getFullFileName(basePath, directoryName)).delete();
        }
        return result;
    }

    private static boolean pasteFile(String basePath, String destinationPath, String fileName, String newFileName, boolean copy){
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(FileUtils.getFullFileName(basePath, fileName));
            out = new FileOutputStream(FileUtils.getFullFileName(destinationPath, newFileName));

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file
            out.flush();
            out.close();

            // delete the original file
            if(!copy)
                new File(FileUtils.getFullFileName(basePath, fileName)).delete();
        }

        catch (FileNotFoundException e) {
            Log.e("ERROR", e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
            return false;
        }
        return true;
    }
}
