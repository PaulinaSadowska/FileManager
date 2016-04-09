package com.nekodev.paulina.sadowska.filemanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class ChooseLanguageActivity extends AppCompatActivity {

    private RadioGroup languageRadioGroup;
    private Button chooseLanguageButton;
    private static final Map<Integer, String> languages;
    static
    {
        languages = new HashMap<>();
        languages.put(R.id.choose_language_polish, "pl");
        languages.put(R.id.choose_language_engish, "en_US");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.choose_language));
        setContentView(R.layout.choose_language_activity);

        languageRadioGroup = (RadioGroup) findViewById(R.id.choose_language_radio_group);
        chooseLanguageButton = (Button) findViewById(R.id.choose_language_button);
        final Activity activity = this;
        chooseLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = languages.get(languageRadioGroup.getCheckedRadioButtonId());
                Toast.makeText(activity, language, Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }
}
