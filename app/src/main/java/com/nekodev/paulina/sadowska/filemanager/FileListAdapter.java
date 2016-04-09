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

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FileListAdapter extends RecyclerView.Adapter<FileViewHolder>  {

    private ArrayList<File> mFileList;
    private Activity mActivity;
    private String path;

    public FileListAdapter(ArrayList<File> fileList, Activity activity, String path){
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
    public void onBindViewHolder(FileViewHolder holder, int position) {
        holder.bindViewHolder(mFileList.get(position));
        holder.setClickListener(new FileViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {

                if (isLongClick)
                    onLongClick(v, pos);
                else
                    onQuickClick(v, pos);
            }
        });
    }

    private void onQuickClick(View v, int pos) {
        File file = mFileList.get(pos);
        String filename = mFileList.get(pos).getName();
        if (path.endsWith(File.separator)) {
            filename = path + filename;
        } else {
            filename = path + File.separator + filename;
        }
        if (!file.canRead()) {
            Toast.makeText(mActivity, filename + " is not accessible", Toast.LENGTH_SHORT).show();
        } else if (file.isDirectory()) {
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
        Toast.makeText(mActivity, "long cick", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }
}
