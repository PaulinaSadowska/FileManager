package com.nekodev.paulina.sadowska.filemanager.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.nekodev.paulina.sadowska.filemanager.R;
import com.nekodev.paulina.sadowska.filemanager.utilities.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paulina Sadowska on 23.04.16.
 */
public class ChooseSortMethodActivity extends AppCompatActivity{

    @Bind(R.id.sorting_methods_spinner)
    Spinner mSortingMethodsSpinner;
    @Bind(R.id.sorting_direction_spinner)
    Spinner mSortingDirectionsSpinner;
    @Bind(R.id.choose_sorting_method_button)
    Button chooseSortingMethodButton;

    private ArrayList<String> sortingMethods;
    private ArrayList<String> sortingDirections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.sorting_method_header));
        setContentView(R.layout.choose_sorting_method_activity);
        ButterKnife.bind(this);

        initSpinnersData();
        chooseSortingMethodButton = (Button) findViewById(R.id.choose_sorting_method_button);
        chooseSortingMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sortBy = (int) mSortingMethodsSpinner.getSelectedItemId();
                int sortDir = (int) mSortingDirectionsSpinner.getSelectedItemId();
                changeSortingMethod(sortBy, sortDir);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void initSpinnersData(){
        sortingMethods = new ArrayList<>();
        sortingMethods.add(getString(R.string.sorting_method_by_name));
        sortingMethods.add(getString(R.string.sorting_method_by_date));
        sortingMethods.add(getString(R.string.sorting_method_by_size));

        sortingDirections = new ArrayList<>();
        sortingDirections.add(getString(R.string.sorting_direction_ascending));
        sortingDirections.add(getString(R.string.sorting_direction_descending));

        mSortingMethodsSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sortingMethods));

        mSortingDirectionsSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sortingDirections));

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int sortMethod = sharedPref.getInt(Constants.SORT_BY_KEY, Constants.SORTING_METHODS.BY_NAME);
        int sortDirection = sharedPref.getInt(Constants.SORT_DIR_KEY, Constants.SORTING_DIRECTION.ASCENDING);
        mSortingDirectionsSpinner.setSelection(sortDirection);
        mSortingMethodsSpinner.setSelection(sortMethod);
    }

    private void changeSortingMethod(Integer sortByMethod, Integer sortDirection) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.SORT_BY_KEY, sortByMethod);
        editor.putInt(Constants.SORT_DIR_KEY, sortDirection);
        editor.apply();
    }
}
