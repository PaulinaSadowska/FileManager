package com.nekodev.paulina.sadowska.filemanager;

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

        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(actionBarToolbar);
        actionBarToolbar.setTitleTextColor(Color.WHITE);
    /*    actionBarToolbar.setNavigationIcon(R.drawable.file);
        actionBarToolbar.setNavigationContentDescription(getString(R.string.app_name));
        actionBarToolbar.setLogo(R.drawable.folder);
        actionBarToolbar.setLogoDescription(getString(R.string.change_language));*/


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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startChooseLanguageActivity() {
        Intent intent = new Intent(this, ChooseLanguageActivity.class);
        startActivity(intent);
    }
}
