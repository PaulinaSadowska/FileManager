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
        DeleteFileThread thread = new DeleteFileThread(fileList);
        thread.run();

    }

    class DeleteFileThread implements Runnable{

        private Map<String, FileType> fileList;

        DeleteFileThread(Map<String, FileType> fileList){
            this.fileList = fileList;
        }

        @Override
        public void run() {
            Iterator it = fileList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if(pair.getValue()==FileType.FILE){
                    String fullPath = getFullFileName(path, (String)pair.getKey());
                    File file = new File(fullPath);
                    if(file.delete())
                        Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();

                }
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
    }
}

