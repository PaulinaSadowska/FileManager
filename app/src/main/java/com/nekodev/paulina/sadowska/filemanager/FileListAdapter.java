package com.nekodev.paulina.sadowska.filemanager;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FileListAdapter extends RecyclerView.Adapter<FileViewHolder> {

    private ArrayList<FileDataItem> mFileList;
    private Activity mActivity;
    private String path;
    private int checkBoxesVisibility = View.GONE;
    private CheckCounter checkCounter = new CheckCounter();

    public FileListAdapter(ArrayList<FileDataItem> fileList, Activity activity, String path){
        this.mActivity = activity;
        this.mFileList = fileList;
        this.path = path;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_list_item, parent, false);
        return new FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, final int position) {
        holder.bindViewHolder(mFileList.get(position));
        holder.mFileCheck.setVisibility(checkBoxesVisibility);
        holder.mFileCheck.setChecked(mFileList.get(position).isChecked());
        holder.setClickListener(new FileViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {

                if (isLongClick)
                    onLongClick(v, pos);
                else
                    onQuickClick(v, pos);
            }

            @Override
            public void onCheckboxClick(View v, int position, boolean isChecked) {
                if(checkCounter.updateCheckCount(isChecked)){
                    hideCheckBoxes();
                }
                mFileList.get(position).setIsChecked(isChecked);
            }
        });
    }


    private void onQuickClick(View v, int pos) {
        FileDataItem file = mFileList.get(pos);
        String filename = mFileList.get(pos).getName();
        if (path.endsWith(File.separator)) {
            filename = path + filename;
        } else {
            filename = path + File.separator + filename;
        }
        if (!file.isReadable()) {
            Toast.makeText(mActivity, filename + " is not accessible", Toast.LENGTH_SHORT).show();
        } else if (file.getType()==FileType.DIRECTORY) {
            Intent intent = new Intent(mActivity, MainActivity.class);
            intent.putExtra("path", filename);
            mActivity.startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            Uri uri = Uri.parse("file://" + file.getAbsolutePath());
            String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
            String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (type != null) {
                intent.setDataAndType(uri, type);
                try {
                    mActivity.startActivityForResult(intent, 10);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(mActivity, "cannot open " + filename + " file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void onLongClick(View v, int pos) {
        if(checkBoxesVisibility==View.GONE)
            showCheckBoxes(pos);
    }

    private void showCheckBoxes(int pos) {
        checkBoxesVisibility = View.VISIBLE;
        mFileList.get(pos).setIsChecked(true);
        checkCounter.updateCheckCount(true);
        notifyDataSetChanged();
    }


    private void hideCheckBoxes() {
        checkBoxesVisibility = View.GONE;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public Map<String, FileType> getCheckedFiles() {
        Map<String, FileType> checkedFiles = new HashMap<>();
        for(FileDataItem file: mFileList){
            if(file.isChecked()){
                checkedFiles.put(file.getName(), file.getType());
            }
        }
        return checkedFiles;
    }
}
