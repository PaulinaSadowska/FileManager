package com.nekodev.paulina.sadowska.filemanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.file_name)     TextView mFileName;
    @Bind(R.id.file_icon)     ImageView mFileIcon;
    @Bind(R.id.file_date)     TextView mFileDate;
    @Bind(R.id.file_size)     TextView mFileSize;
    @Bind(R.id.file_checkbox) CheckBox mFileCheck;


    private ClickListener clickListener;
    private CheckListener checkListener;

    public FileViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        mFileCheck.setOnCheckedChangeListener(this);
    }

    public void bindViewHolder(FileDataItem file){
        mFileName.setText(file.getName());

        if(file.getType()==FileType.FILE)
            mFileIcon.setImageResource(R.drawable.file);
        else if(file.getType()==FileType.DIRECTORY)
            mFileIcon.setImageResource(R.drawable.folder);
        else
            mFileIcon.setImageResource(R.drawable.file);


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

    public interface CheckListener{
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, int position);
    }

    public void setCheckListener(CheckListener checkListener){
        this.checkListener = checkListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkListener.onCheckedChanged(buttonView, isChecked, getPosition());
    }

}
