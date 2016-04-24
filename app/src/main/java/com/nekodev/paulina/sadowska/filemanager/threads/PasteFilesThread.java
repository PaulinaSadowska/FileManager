package com.nekodev.paulina.sadowska.filemanager.threads;

import com.nekodev.paulina.sadowska.filemanager.data.FileType;
import com.nekodev.paulina.sadowska.filemanager.utilities.FileUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Paulina Sadowska on 23.04.16.
 */
public class PasteFilesThread implements Runnable{

    private Map<String, FileType> fileList;
    private String basePath;
    private String destinationPath;
    private ThreadListener listener;

    public PasteFilesThread(Map<String, FileType> fileList, String basePath, String destinationPath){
        this.fileList = fileList;
        this.basePath = basePath;
        this.destinationPath = destinationPath;
    }

    public void addCompleteListener(ThreadListener listener){
        this.listener = listener;
    }


    @Override
    public void run() {
        Iterator it = fileList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            moveWithChildren(FileUtils.getFullFileName(basePath, (String) pair.getKey()), (FileType)pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        if(listener!=null)
            listener.notifyOfThreadComplete(this);
    }

    private boolean moveWithChildren(String fullPath, FileType fileType) {
        if(fileType==FileType.FILE){
            //return moveFile(fullPath);
        }
        //if(fileType==FileType.DIRECTORY){
        //    return moveDirectory(fullPath);
        //}
        return false; //unknown type or cannot read
    }

  /*  private boolean moveDirectory(String path) {
        ArrayList<File> fileList =  FileUtils.getListOfFiles(path);
        boolean result = true;
        for(File file: fileList){
            result = (result && moveWithChildren(file.getPath(), FileUtils.getFileType(file)));
        }
        if(result){
            File dir = new File(path);
            result = dir.move();
        }
        return result;
    }*/

    /*private boolean moveFile(String fullPath){
        File file = new File(fullPath);
        return file.move;
    }*/
}
