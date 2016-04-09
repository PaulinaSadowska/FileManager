package com.nekodev.paulina.sadowska.filemanager;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FilesFragment filesFragment = new FilesFragment();
        transaction.replace(R.id.files_fragment_container, filesFragment);
        transaction.commit();
    }

}
