package com.nekodev.paulina.sadowska.filemanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private TextView mFileName;
    private ImageView mFileIcon;

    private ClickListener clickListener;

    public FileViewHolder(View itemView) {
        super(itemView);
        mFileName = (TextView) itemView.findViewById(R.id.file_name);
        mFileIcon = (ImageView) itemView.findViewById(R.id.file_icon);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void bindViewHolder(File file){
        mFileName.setText(file.getName());
        if(file.isFile())
            mFileIcon.setBackgroundResource(R.drawable.file);
        else if(file.isDirectory())
            mFileIcon.setBackgroundResource(R.drawable.folder);
    }

    /* Interface for handling clicks - both normal and long ones. */
    public interface ClickListener {
        public void onClick(View v, int position, boolean isLongClick);
    }

    /* Setter for listener. */
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick(v, getPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {
        clickListener.onClick(v, getPosition(), true);
        return true;
    }
}
