package com.nekodev.paulina.sadowska.filemanager;

import android.content.Intent;
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
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FilesFragment extends Fragment {

    @Bind(R.id.files_list_recycler_view) RecyclerView mAlarmRecyclerView;
    private FileDataItemFactory mFileDataFactory;
    private FileListAdapter mFileAdapter;
    private String path = "/";

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

        ArrayList<File> fileList = FileUtils.getListOfFiles(path);

        Collections.sort(fileList);
        ArrayList<FileDataItem> fileDataList = new ArrayList<>();
        for(File f : fileList){
            fileDataList.add(mFileDataFactory.createFileDataItem(f));
        }

        mFileAdapter = new FileListAdapter(fileDataList, getActivity(), path);
        mAlarmRecyclerView.setAdapter(mFileAdapter);
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
        DeleteFilesThread thread = new DeleteFilesThread(fileList, path);
        thread.addCompleteListener(new ThreadCompleteListener() {
            @Override
            public void notifyOfThreadComplete(Runnable runnable) {
                Intent refresh = new Intent(getActivity(), MainActivity.class);
                refresh.putExtra("path", path);
                getActivity().startActivity(refresh);
            }
        });
        thread.run();
    }
}

