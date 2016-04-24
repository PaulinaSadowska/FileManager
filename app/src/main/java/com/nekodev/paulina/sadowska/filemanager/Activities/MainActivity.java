package com.nekodev.paulina.sadowska.filemanager.activities;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.nekodev.paulina.sadowska.filemanager.FilesFragment;
import com.nekodev.paulina.sadowska.filemanager.R;
import com.nekodev.paulina.sadowska.filemanager.utilities.Constants;

import java.io.File;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class MainActivity extends AppCompatActivity {


    private static final int CHOOSE_LANGUAGE_CODE = 999;
    private static final int CHOOSE_SORTING_METHOD_CODE = 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FilesFragment filesFragment = new FilesFragment();
        transaction.replace(R.id.files_fragment_container, filesFragment);
        transaction.commit();

        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(actionBarToolbar);
        actionBarToolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.choose_language:
                startChooseLanguageActivity();
                return true;
            case R.id.choose_sorting_method:
                startChooseSortingMethodActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startChooseLanguageActivity() {
        Intent intent = new Intent(this, ChooseLanguageActivity.class);
        startActivityForResult(intent, CHOOSE_LANGUAGE_CODE);
    }

    private void startChooseSortingMethodActivity() {
        Intent intent = new Intent(this, ChooseSortMethodActivity.class);
        startActivityForResult(intent, CHOOSE_SORTING_METHOD_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CHOOSE_LANGUAGE_CODE || requestCode == CHOOSE_SORTING_METHOD_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                refreshActivity(getIntent().getStringExtra(Constants.INTENT_KEYS.PATH));
            }
        }
    }

    private void refreshActivity(String path) {
        finish();
        Intent refresh = new Intent(this, MainActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        refresh.putExtra(Constants.INTENT_KEYS.PATH, path);
        startActivity(refresh);
    }

    @Override
    public void onBackPressed() {
        File current = new File(getIntent().getStringExtra(Constants.INTENT_KEYS.PATH));
        if(current.getName().length()>1)
            refreshActivity(current.getParent());
        else
            finish();
    }
}
