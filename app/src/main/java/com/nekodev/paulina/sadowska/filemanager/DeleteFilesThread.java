package com.nekodev.paulina.sadowska.filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Paulina Sadowska on 21.04.16.
 */
public class DeleteFilesThread implements Runnable{

    private Map<String, FileType> fileList;
    private String basePath;
    private ThreadCompleteListener listener;

    DeleteFilesThread(Map<String, FileType> fileList, String basePath){
        this.fileList = fileList;
        this.basePath = basePath;
    }

    public void addCompleteListener(ThreadCompleteListener listener){
        this.listener = listener;
    }

    @Override
    public void run() {
        Iterator it = fileList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            deleteWithChildren(FileUtils.getFullFileName(basePath, (String) pair.getKey()), (FileType)pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        if(listener!=null)
            listener.notifyOfThreadComplete(this);
    }

    private boolean deleteWithChildren(String path, FileType fileType) {
        if(fileType==FileType.FILE){
            return deleteFile(path);
        }
        if(fileType==FileType.DIRECTORY){
            return deleteDirectory(path);
        }
        return false; //unknown type or cannot read
    }

    private boolean deleteDirectory(String path) {
        ArrayList<File> fileList =  FileUtils.getListOfFiles(path);
        boolean result = true;
        for(File file: fileList){
            result = (result && deleteWithChildren(file.getPath(), FileUtils.getFileType(file)));
        }
        if(result){
            File dir = new File(path);
            result = dir.delete();
        }
        return result;
    }

    private boolean deleteFile(String fullPath){
        File file = new File(fullPath);
        return file.delete();
    }
}
