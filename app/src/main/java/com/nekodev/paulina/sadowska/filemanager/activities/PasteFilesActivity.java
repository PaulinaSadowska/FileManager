package com.nekodev.paulina.sadowska.filemanager.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nekodev.paulina.sadowska.filemanager.R;
import com.nekodev.paulina.sadowska.filemanager.data.FileType;
import com.nekodev.paulina.sadowska.filemanager.utilities.Constants;
import com.nekodev.paulina.sadowska.filemanager.utilities.FilePasteUtils;
import com.nekodev.paulina.sadowska.filemanager.utilities.FileUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    private String destinationPath;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog_activity);
        ButterKnife.bind(this);

        destinationPath = getIntent().getExtras().getString(Constants.INTENT_KEYS.PATH);

        getCopiedFilesList();
        setDialogTitle();
        mProgressBar.setProgress(0);
        mProgressMessage.setText(getProgressMessage(0));
        mActivity = this;
        if(isPossible()) {
            new CopyFilesTask().execute();
        }
        else{
            Toast.makeText(this, getString(R.string.error_paste_inside_base_subfolder), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean isPossible() {
        return !(destinationPath.startsWith(basePath + "/") && !copy);
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
    }

    private class CopyFilesTask extends AsyncTask<Void, Integer, Boolean> {
        /**
         * The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute()
         */
        protected Boolean doInBackground(Void... urls) {
            Iterator it = fileList.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                FilePasteUtils.pasteWithChildren(basePath, destinationPath, (String) pair.getKey(), (FileType) pair.getValue(), copy);
                publishProgress(++i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                it.remove(); // avoids a ConcurrentModificationException
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressMessage.setText(getProgressMessage(values[0]));
            mProgressBar.setProgress((values[0] * 100) / count);
        }

        /**
         * The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground()
         */
        protected void onPostExecute(Boolean result) {
            mProgressMessage.setText("COMPLETED");
            mProgressBar.setProgress(100);

            Intent refresh = new Intent(mActivity, MainActivity.class);
            refresh.putExtra(Constants.INTENT_KEYS.PATH, destinationPath);
            mActivity.startActivity(refresh);
            finish();

        }
    }

}
