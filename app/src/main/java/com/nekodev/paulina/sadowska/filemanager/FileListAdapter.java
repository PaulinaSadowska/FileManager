package com.nekodev.paulina.sadowska.filemanager;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.nekodev.paulina.sadowska.filemanager.activities.MainActivity;
import com.nekodev.paulina.sadowska.filemanager.data.FileDataItem;
import com.nekodev.paulina.sadowska.filemanager.data.FileType;
import com.nekodev.paulina.sadowska.filemanager.utilities.CheckCounter;
import com.nekodev.paulina.sadowska.filemanager.utilities.Constants;
import com.nekodev.paulina.sadowska.filemanager.utilities.FileUtils;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FileListAdapter extends RecyclerView.Adapter<FileViewHolder> {

    private LinkedList<FileDataItem> mFileList;
    private HashMap<Integer, Bitmap> mFileIcons = new HashMap<>();
    private Activity mActivity;
    private String path;
    private int checkBoxesVisibility = View.GONE;
    private CheckCounter checkCounter = new CheckCounter();

    public FileListAdapter(LinkedList<FileDataItem> fileList, Activity activity, String path){
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
        Bitmap icon = mFileIcons.get(position);
        if(icon != null) {
            holder.bindViewHolder(mFileList.get(position), icon);
        }
        else{
            holder.bindViewHolder(mFileList.get(position));
        }
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
                if (checkCounter.updateCheckCount(isChecked)) {
                    hideCheckBoxes();
                }
                mFileList.get(position).setIsChecked(isChecked);
            }
        });
    }
    
    public void setIconFromImageResource(int position, Bitmap bitmap){
        mFileIcons.put(position, bitmap);
        notifyItemChanged(position);
    }


    private void onQuickClick(View v, int pos) {
        FileDataItem file = mFileList.get(pos);
        String filename = mFileList.get(pos).getName();
        filename = FileUtils.getFullFileName(path, filename);
        if (!file.isReadable()) {
            Toast.makeText(mActivity, filename + " " + mActivity.getString(R.string.file_not_accessible), Toast.LENGTH_SHORT).show();
        } else if (file.getType()== FileType.DIRECTORY) {
            Intent intent = new Intent(mActivity, MainActivity.class);
            intent.putExtra(Constants.INTENT_KEYS.PATH, filename);
            mActivity.startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            Uri uri = Uri.parse("file://" + file.getAbsolutePath());
            String extension = FileUtils.getFileExtension(file.getName());
            String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (type != null) {
                intent.setDataAndType(uri, type);
                try {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mActivity.startActivityForResult(intent, 10);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(mActivity, mActivity.getString(R.string.cannot_open_1) + " " + filename + " " + mActivity.getString(R.string.cannot_open_2), Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(mActivity, mActivity.getString(R.string.cannot_open_1) + " " + filename + " " + mActivity.getString(R.string.cannot_open_2), Toast.LENGTH_SHORT).show();
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

    public HashMap<String, FileType> getCheckedFiles() {
        HashMap<String, FileType> checkedFiles = new HashMap<>();
        for(FileDataItem file: mFileList){
            if(file.isChecked()){
                FileType type = file.getType();
                if(!file.isReadable())
                    type = FileType.UNKNOWN;

                checkedFiles.put(file.getName(), type);
            }
        }
        return checkedFiles;
    }

    public int getCheckedItemCount() {
        return checkCounter.getCheckCount();
    }

    public void hideAllCheckBoxes() {
        for (FileDataItem fileDataItem : mFileList) {
            fileDataItem.setIsChecked(false);
        }
        hideCheckBoxes();
    }
}
