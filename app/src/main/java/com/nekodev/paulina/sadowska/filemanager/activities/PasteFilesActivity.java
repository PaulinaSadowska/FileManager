package com.nekodev.paulina.sadowska.filemanager.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nekodev.paulina.sadowska.filemanager.R;
import com.nekodev.paulina.sadowska.filemanager.data.FileType;
import com.nekodev.paulina.sadowska.filemanager.utilities.Constants;
import com.nekodev.paulina.sadowska.filemanager.utilities.FileUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paulina Sadowska on 23.04.16.
 */
public class PasteFilesActivity extends AppCompatActivity {

    @Bind(R.id.loading_progress_message)
    TextView mProgressMessage;
    @Bind(R.id.loading_progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.loading_progress_cancel_button)
    Button mCancelButton;

    private HashMap<String, FileType> fileList;
    private boolean copy;
    private int count;
    private String basePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog_activity);
        ButterKnife.bind(this);

        getCopiedFilesList();
        setDialogTitle();
        mProgressBar.setProgress(0);
        mProgressMessage.setText(getProgressMessage(0));
    }

    private String getProgressMessage(int i) {
        if(copy)
            return getString(R.string.copy_dialog_message) + " "+i+"/"+count;

        return getString(R.string.cut_dialog_message) + " "+i+"/"+count;

    }

    private void setDialogTitle() {
        if(copy)
            setTitle(getString(R.string.copy_dialog_title));
        else
            setTitle(getString(R.string.cut_dialog_title));
    }


    private void getCopiedFilesList() {

        fileList = new HashMap<>();

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        copy = sharedPref.getBoolean(Constants.SELECTED_FILES.COPY_OR_CUT, false);
        count = sharedPref.getInt(Constants.SELECTED_FILES.COUNT, 0);
        basePath = sharedPref.getString(Constants.SELECTED_FILES.PATH, "");

        for (int i = 0; i < count; i++) {
            String fileName = sharedPref.getString(Constants.SELECTED_FILES.KEY+i, "");
            String fileType = sharedPref.getString(Constants.SELECTED_FILES.TYPE+i, "");
            fileList.put(fileName, FileUtils.getFileType(fileType));
        }
        Toast.makeText(this, "wklejono pliki: " + count, Toast.LENGTH_SHORT).show();
    }

}
