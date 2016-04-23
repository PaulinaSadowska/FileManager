package com.nekodev.paulina.sadowska.filemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paulina Sadowska on 23.04.16.
 */
public class ChooseSortMethodActivity extends AppCompatActivity{

    private RadioGroup sortingMethodsRadioGroup;
    private Button chooseSortingMethodButton;
    private static final Map<Integer, Integer> sortingMethods;
    static
    {
        sortingMethods = new HashMap<>();
        sortingMethods.put(R.id.sorting_method_by_name, Constants.SORTING_METHODS.BY_NAME);
        sortingMethods.put(R.id.sorting_method_by_date, Constants.SORTING_METHODS.BY_DATE);
        sortingMethods.put(R.id.sorting_method_by_size, Constants.SORTING_METHODS.BY_SIZE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.sorting_method_header));
        setContentView(R.layout.choose_sorting_method_activity);

        sortingMethodsRadioGroup = (RadioGroup) findViewById(R.id.sorting_methods_radio_group);
        chooseSortingMethodButton = (Button) findViewById(R.id.choose_sorting_method_button);
        chooseSortingMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer sortBy = sortingMethods.get(sortingMethodsRadioGroup.getCheckedRadioButtonId());
                changeSortingMethod(sortBy);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void changeSortingMethod(Integer sortByMethod) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.SORT_BY_KEY, sortByMethod);
        editor.apply();
    }
}
