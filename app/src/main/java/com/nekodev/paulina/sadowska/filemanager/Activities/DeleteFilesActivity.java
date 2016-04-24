package com.nekodev.paulina.sadowska.filemanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nekodev.paulina.sadowska.filemanager.R;
import com.nekodev.paulina.sadowska.filemanager.data.FileType;
import com.nekodev.paulina.sadowska.filemanager.utilities.Constants;
import com.nekodev.paulina.sadowska.filemanager.utilities.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paulina Sadowska on 23.04.16.
 */
public class DeleteFilesActivity extends AppCompatActivity {

    @Bind(R.id.loading_progress_message)
    TextView mProgressMessage;
    @Bind(R.id.loading_progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.loading_progress_cancel_button)
    Button mCancelButton;
    boolean interrupt = false;

    private HashMap<String, FileType> fileList;
    private String basePath;
    private int count;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog_activity);
        ButterKnife.bind(this);

        basePath = getIntent().getExtras().getString(Constants.INTENT_KEYS.PATH);
        fileList = (HashMap<String, FileType>) getIntent().getExtras().get(Constants.INTENT_KEYS.FILES_TO_DELETE);
        count = fileList != null ? fileList.keySet().size() : 0;

        setTitle(getString(R.string.delete_dialog_title));
        mProgressBar.setProgress(0);
        mProgressMessage.setText(getProgressMessage(0));
        mActivity = this;
        if(isPossible()) {
            new DeleteFilesTask().execute();
        }
        else{
            Toast.makeText(this, getString(R.string.error_deleting_files), Toast.LENGTH_SHORT).show();
            finish();
        }
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrupt = true;
            }
        });
    }

    private boolean isPossible() {
        return count>0;
    }

    private String getProgressMessage(int i) {
        return getString(R.string.delete_dialog_message) + " "+i+"/"+count;
    }

    private class DeleteFilesTask extends AsyncTask<Void, Integer, Boolean> {
        /**
         * The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute()
         */
        protected Boolean doInBackground(Void... urls) {
            Iterator it = fileList.entrySet().iterator();
            int i = 0;
            while (it.hasNext() && !interrupt) {
                Map.Entry pair = (Map.Entry) it.next();
                deleteWithChildren(FileUtils.getFullFileName(basePath, (String) pair.getKey()), (FileType) pair.getValue());
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
            mProgressMessage.setText(getString(R.string.completed));
            mProgressBar.setProgress(100);

            Intent refresh = new Intent(mActivity, MainActivity.class);
            refresh.putExtra(Constants.INTENT_KEYS.PATH, basePath);
            mActivity.startActivity(refresh);
            finish();

        }

        private boolean deleteWithChildren(String path, FileType fileType) {
            if(fileType==FileType.FILE){
                return deleteFile(path);
            }
            if(fileType==FileType.DIRECTORY){
                return deleteDirectory(path);
            }
            return false; //unknown type or cannot read
        }

        private boolean deleteDirectory(String path) {
            ArrayList<File> fileList =  FileUtils.getListOfFiles(path);
            boolean result = true;
            for(File file: fileList){
                result = (result && deleteWithChildren(file.getPath(), FileUtils.getFileType(file)));
            }
            if(result){
                File dir = new File(path);
                result = dir.delete();
            }
            return result;
        }

        private boolean deleteFile(String fullPath){
            File file = new File(fullPath);
            return file.delete();
        }
    }

}
