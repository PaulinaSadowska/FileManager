package com.nekodev.paulina.sadowska.filemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;

/**
 * Created by Paulina Sadowska on 23.04.16.
 */
public class LoadingActivity extends AppCompatActivity {

    @Bind(R.id.loading_progress_message)
    TextView mProgressMessage;
    @Bind(R.id.loading_progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog_activity);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
