package com.nekodev.paulina.sadowska.filemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.nekodev.paulina.sadowska.filemanager.activities.MainActivity;
import com.nekodev.paulina.sadowska.filemanager.data.FileDataItem;
import com.nekodev.paulina.sadowska.filemanager.data.FileType;
import com.nekodev.paulina.sadowska.filemanager.data.factories.FileDataItemFactory;
import com.nekodev.paulina.sadowska.filemanager.threads.DeleteFilesThread;
import com.nekodev.paulina.sadowska.filemanager.threads.ThreadCompleteListener;
import com.nekodev.paulina.sadowska.filemanager.utilities.Constants;
import com.nekodev.paulina.sadowska.filemanager.utilities.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FilesFragment extends Fragment {

    @Bind(R.id.files_list_recycler_view)
    RecyclerView mAlarmRecyclerView;
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
        if (getActivity().getIntent().hasExtra(Constants.INTENT_KEYS.PATH)) {
            path = getActivity().getIntent().getStringExtra(Constants.INTENT_KEYS.PATH);
        }
        else{
            getActivity().getIntent().putExtra(Constants.INTENT_KEYS.PATH, path);
        }
        getActivity().setTitle(path);

        mFileAdapter = new FileListAdapter(getSortedFileDataList(), getActivity(), path);
        mAlarmRecyclerView.setAdapter(mFileAdapter);
    }

    private ArrayList<FileDataItem> getSortedFileDataList() {

        ArrayList<File> fileList = FileUtils.getListOfFiles(path);
        ArrayList<FileDataItem> fileDataList = new ArrayList<>();
        for (File f : fileList) {
            fileDataList.add(mFileDataFactory.createFileDataItem(f));
        }

        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int sortMethod = sharedPref.getInt(Constants.SORT_BY_KEY, Constants.SORTING_METHODS.BY_NAME);

        switch(sortMethod){
            case(Constants.SORTING_METHODS.BY_NAME):
                fileDataList = FileUtils.sortByName(fileDataList, Constants.SORTING_DIRECTION.ASCENDING);
                break;
            case(Constants.SORTING_METHODS.BY_DATE):
                fileDataList = FileUtils.sortByDate(fileDataList, Constants.SORTING_DIRECTION.DESCENDING);
                break;
            case(Constants.SORTING_METHODS.BY_SIZE):
                fileDataList = FileUtils.sortBySize(fileDataList, Constants.SORTING_DIRECTION.ASCENDING);
                break;
        }
        return fileDataList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteCheckedFiles();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteCheckedFiles() {
        if(mFileAdapter.getCheckedItemCount()>0) {
            createDeleteDialog();
        }
        else{
            Toast.makeText(getActivity(), getString(R.string.no_files_selected), Toast.LENGTH_SHORT).show();
        }
    }

    private void createDeleteDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.delete_alert_title))
                .setMessage(getResources().getString(R.string.delete_alert_body) + mFileAdapter.getCheckedItemCount() + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFiles();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteFiles() {
        HashMap<String, FileType> fileList = mFileAdapter.getCheckedFiles();
        DeleteFilesThread thread = new DeleteFilesThread(fileList, path);
        thread.addCompleteListener(new ThreadCompleteListener() {
            @Override
            public void notifyOfThreadComplete(Runnable runnable) {
                Intent refresh = new Intent(getActivity(), MainActivity.class);
                refresh.putExtra(Constants.INTENT_KEYS.PATH, path);
                getActivity().startActivity(refresh);
            }
        });
        thread.run();
    }


}

