package com.nekodev.paulina.sadowska.filemanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FilesFragment extends Fragment {

    @Bind(R.id.files_list_recycler_view) RecyclerView mAlarmRecyclerView;
    private FileDataItemFactory mFileDataFactory;
    FileListAdapter mFileAdapter;
    String path = "/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.files_fragment, container, false);
        setHasOptionsMenu(true);
        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mFileDataFactory = new FileDataItemFactory(getActivity());
        LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAlarmRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Use the current directory as title
        if (getActivity().getIntent().hasExtra("path")) {
            path = getActivity().getIntent().getStringExtra("path");
        }
        getActivity().setTitle(path);

        ArrayList<File> fileList = getListOfFiles(path);

        Collections.sort(fileList);
        ArrayList<FileDataItem> fileDataList = new ArrayList<>();
        for(File f : fileList){
            fileDataList.add(mFileDataFactory.createFileDataItem(f));
        }

        mFileAdapter = new FileListAdapter(fileDataList, getActivity(), path);
        mAlarmRecyclerView.setAdapter(mFileAdapter);
    }

    private ArrayList<File> getListOfFiles(String path) {
        File dir = new File(path);
        String[] list = dir.list();
        ArrayList<File> fileList = new ArrayList<>();
        if (list != null) {
            for (String fileName : list) {
                if (!fileName.startsWith(".")) {
                    fileList.add(new File(getFullFileName(path, fileName)));
                }
            }
        }
        return fileList;
    }

    private String getFullFileName(String path, String fileName) {
        if (path.endsWith(File.separator)) {
            return (path + fileName);
        } else {
            return (path + File.separator + fileName);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteCheckedFiles();
                Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteCheckedFiles() {
        Map<String, FileType> fileList = mFileAdapter.getCheckedFiles();
        DeleteFilesThread thread = new DeleteFilesThread(fileList);
        thread.run();

    }

    private class DeleteFilesThread implements Runnable{

        private Map<String, FileType> fileList;

        DeleteFilesThread(Map<String, FileType> fileList){
            this.fileList = fileList;
        }

        @Override
        public void run() {
            Iterator it = fileList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                deleteWithChildren(getFullFileName(path, (String)pair.getKey()), (FileType)pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
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
            ArrayList<File> fileList =  getListOfFiles(path);
            boolean result = true;
            for(File file: fileList){
                result = (result && deleteWithChildren(file.getPath(), getFileType(file)));
            }
            if(result){
                File dir = new File(path);
                result = dir.delete();
            }
            return result;
        }

        private FileType getFileType(File file) {
            if(!file.canRead())
                return FileType.UNKNOWN;
            if(file.isFile())
                return FileType.FILE;
            if(file.isDirectory())
                return FileType.DIRECTORY;
            return FileType.UNKNOWN;
        }

        private boolean deleteFile(String fullPath){
            File file = new File(fullPath);
            return file.delete();
        }
    }
}

