package com.nekodev.paulina.sadowska.filemanager;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nekodev.paulina.sadowska.filemanager.data.FileDataItem;
import com.nekodev.paulina.sadowska.filemanager.data.FileType;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @Bind(R.id.file_name)     TextView mFileName;
    @Bind(R.id.file_icon)     ImageView mFileIcon;
    @Bind(R.id.file_date)     TextView mFileDate;
    @Bind(R.id.file_size)     TextView mFileSize;
    @Bind(R.id.file_checkbox) CheckBox mFileCheck;


    private ClickListener clickListener;

    public FileViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        mFileCheck.setOnClickListener(this);
    }

    public void bindViewHolder(FileDataItem file){
        mFileName.setText(file.getName());

        if(file.getType()== FileType.FILE)
            mFileIcon.setImageResource(R.drawable.file);
        else if(file.getType()==FileType.DIRECTORY)
            mFileIcon.setImageResource(R.drawable.folder);
        else
            mFileIcon.setImageResource(R.drawable.unknown);

        mFileDate.setText(file.getLastModifiedString());
        mFileSize.setText(file.getSizeString());
    }

    public void bindViewHolder(FileDataItem file, Bitmap icon){
        this.bindViewHolder(file);
        mFileIcon.setImageBitmap(icon);
    }




    /* Interface for handling clicks - both normal and long ones. */
    public interface ClickListener {
        public void onClick(View v, int position, boolean isLongClick);
        public void onCheckboxClick(View v, int position, boolean isChecked);
    }

    /* Setter for listener. */
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        if(v instanceof CheckBox) {
            clickListener.onCheckboxClick(v, getPosition(), mFileCheck.isChecked());
        }
        else{
            clickListener.onClick(v, getPosition(), false);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        clickListener.onClick(v, getPosition(), true);
        return true;
    }

}
