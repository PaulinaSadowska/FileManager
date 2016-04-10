package com.nekodev.paulina.sadowska.filemanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private TextView mFileName;
    private ImageView mFileIcon;
    private TextView mFileDate;
    private TextView mFileSize;


    private ClickListener clickListener;

    public FileViewHolder(View itemView) {
        super(itemView);
        mFileName = (TextView) itemView.findViewById(R.id.file_name);
        mFileIcon = (ImageView) itemView.findViewById(R.id.file_icon);
        mFileDate = (TextView) itemView.findViewById(R.id.file_date);
        mFileSize = (TextView) itemView.findViewById(R.id.file_size);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void bindViewHolder(FileDataItem file){
        mFileName.setText(file.getName());

        if(file.getType()==FileType.FILE)
            mFileIcon.setBackgroundResource(R.drawable.file);
        else if(file.getType()==FileType.DIRECTORY)
            mFileIcon.setBackgroundResource(R.drawable.folder);


        mFileDate.setText(file.getLastModified());
        mFileSize.setText(file.getSize());
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
