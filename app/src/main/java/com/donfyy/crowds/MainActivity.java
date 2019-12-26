package com.donfyy.crowds;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import dagger.android.DaggerActivity;

public class MainActivity extends DaggerActivity {
    @Inject
    AppComponentA mAppComponentA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(this.getClass().getSimpleName(), "injected!" + mAppComponentA);
    }
}
