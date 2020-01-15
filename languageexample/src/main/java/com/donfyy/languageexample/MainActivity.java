package com.donfyy.languageexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String LANGUAGE = "language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences sharedPreferences = newBase.getSharedPreferences("common", MODE_PRIVATE);
        Locale locale = newBase.getResources().getConfiguration().locale;
        String language = sharedPreferences.getString(LANGUAGE, locale.getCountry());
        if (locale.getCountry().equals(language)) {
            super.attachBaseContext(newBase);
            return;
        }

        if (Locale.SIMPLIFIED_CHINESE.getCountry().equals(language)) {
            newBase.getResources().getConfiguration().setLocale(Locale.SIMPLIFIED_CHINESE);
        } else {
            newBase.getResources().getConfiguration().setLocale(Locale.ENGLISH);
        }
        Context configurationContext = newBase.createConfigurationContext(newBase.getResources().getConfiguration());

        super.attachBaseContext(configurationContext);
    }

    public void onSwitchLanguage(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("common", MODE_PRIVATE);
        Locale locale = getResources().getConfiguration().locale;
        String language = sharedPreferences.getString(LANGUAGE, locale.getCountry());

        if (Locale.SIMPLIFIED_CHINESE.getCountry().equals(language)) {
            sharedPreferences.edit().putString(LANGUAGE, Locale.ENGLISH.getCountry()).apply();
        } else {
            sharedPreferences.edit().putString(LANGUAGE, Locale.SIMPLIFIED_CHINESE.getCountry()).apply();
        }

        recreate();
    }
}
