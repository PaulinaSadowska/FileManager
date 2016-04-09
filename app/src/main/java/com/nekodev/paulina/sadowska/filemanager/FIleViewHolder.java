package com.nekodev.paulina.sadowska.filemanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mFileName;

    private ClickListener clickListener;

    public FileViewHolder(View itemView) {
        super(itemView);
        mFileName = (TextView) itemView.findViewById(R.id.file_name);
        itemView.setOnClickListener(this);
    }

    public void bindViewHolder(String fileName){
        this.mFileName.setText(fileName);
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
}
