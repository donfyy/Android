package com.donfyy.crowds;

import android.util.Log;

import com.donfyy.crowds.dagger.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class App extends DaggerApplication {
    @Inject
    AppComponentA mAppComponentA;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(App.class.getSimpleName(), "injected!" + mAppComponentA);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
