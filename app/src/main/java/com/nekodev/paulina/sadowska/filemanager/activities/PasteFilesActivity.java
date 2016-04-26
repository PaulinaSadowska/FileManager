package com.nekodev.paulina.sadowska.filemanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.nekodev.paulina.sadowska.filemanager.utilities.FilePasteUtils;
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
    private boolean interrupt = false;

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
        if (isPossible()) {
            new CopyFilesTask().execute();
        } else {
            Toast.makeText(this, getString(R.string.error_paste_inside_base_subfolder), Toast.LENGTH_SHORT).show();
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
        return !(destinationPath.startsWith(basePath + "/") && !copy);
    }

    private String getProgressMessage(int i) {
        if (copy)
            return getString(R.string.copy_dialog_message) + " " + i + "/" + count;

        return getString(R.string.cut_dialog_message) + " " + i + "/" + count;

    }

    private void setDialogTitle() {
        if (copy)
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
            String fileName = sharedPref.getString(Constants.SELECTED_FILES.KEY + i, "");
            String fileType = sharedPref.getString(Constants.SELECTED_FILES.TYPE + i, "");
            fileList.put(fileName, FileUtils.getFileType(fileType));
        }
    }

    private class CopyFilesTask extends AsyncTask<Void, String, Boolean> {

        private final Object lock = new Object();
        private String newFileName;

        protected Boolean doInBackground(Void... urls) {
            ArrayList<String> filesInDestDirectory = FileUtils.getListOfFileNames(destinationPath);
            Iterator it = fileList.entrySet().iterator();
            int i = 0;
            while (it.hasNext() && !interrupt) {
                Map.Entry pair = (Map.Entry) it.next();
                newFileName = (String) pair.getKey();
                String oldName = newFileName;
                i++;
                if (filesInDestDirectory.contains(newFileName)) {
                    publishProgress(i + "", newFileName, "error");
                    lock();
                }
                if (!newFileName.equals("")) {
                    if (FilePasteUtils.pasteWithChildren(basePath, destinationPath, oldName, newFileName, (FileType) pair.getValue(), copy))
                        publishProgress(i + "");
                    else {
                        publishProgress(i + "", newFileName);
                        lock();
                    }
                }
                it.remove(); // avoids a ConcurrentModificationException
                sleep();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            int progress = Integer.parseInt(values[0]);
            if (values.length < 2) {
                mProgressMessage.setText(getProgressMessage(progress));
                mProgressBar.setProgress((progress * 100) / count);
            } else if (values.length < 3) {
                new AlertDialog.Builder(mActivity)
                        .setTitle(getResources().getString(R.string.paste_alert_title))
                        .setMessage(getResources().getString(R.string.error_paste) + " " + values[1])
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                unlock();
                            }
                        })
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .show();
            } else {
                final String fileName = values[1];
                new AlertDialog.Builder(mActivity)
                        .setTitle(getResources().getString(R.string.paste_alert_title))
                        .setMessage(getResources().getString(R.string.error_paste_file_exists_1) + " " +
                                values[1] + " " + getResources().getString(R.string.error_paste_file_exists_2))
                        .setPositiveButton(R.string.paste_keep_both, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                newFileName = renameFile(fileName);
                                unlock();
                            }
                        })
                        .setNeutralButton(R.string.paste_skip, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                newFileName = "";
                                unlock();
                            }
                        })
                        .setNegativeButton(R.string.paste_overwrite, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                unlock();
                            }
                        })
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setCancelable(false)
                        .show();
            }
        }

        private String renameFile(String fileName) {
            File file = new File(FileUtils.getFullFileName(basePath, fileName));
            if (file.isFile()) {
                int pos = fileName.lastIndexOf(".");
                String name = "";
                String extension = "";
                if (pos > 0) {
                    name = fileName.substring(0, pos);
                    extension = fileName.substring(pos + 1, fileName.length());
                }
                return name + " copy." + extension;
            }
            else if (file.isDirectory())
                return fileName + " copy";

            return fileName;

        }

        private void lock() {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void unlock() {
            synchronized (lock) {
                lock.notify();
            }
        }

        private void sleep() {
            if (Constants.SLEEP) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground()
         */
        protected void onPostExecute(Boolean result) {
            mProgressMessage.setText(getString(R.string.completed));
            mProgressBar.setProgress(100);

            Intent refresh = new Intent(mActivity, MainActivity.class);
            refresh.putExtra(Constants.INTENT_KEYS.PATH, destinationPath);
            mActivity.startActivity(refresh);
            finish();

        }
    }

}
