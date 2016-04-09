package com.nekodev.paulina.sadowska.filemanager;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FileListAdapter extends RecyclerView.Adapter<FileViewHolder>  {

    private ArrayList<String> mFileList;
    private Activity mActivity;

    public FileListAdapter(ArrayList<String> fileList, Activity activity){
        this.mActivity = activity;
        this.mFileList = fileList;
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
                Toast.makeText(mActivity, mFileList.get(pos), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }
}
