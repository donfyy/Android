package com.donfyy.crowds;

import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;
import android.view.Choreographer;

import com.donfyy.crowds.dagger.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class App extends DaggerApplication {
    @Inject
    AppComponentA mAppComponentA;

    public App() {
        Debug.startMethodTracing("aaa_trace");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {

            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }
        }.execute();

        Log.e(App.class.getSimpleName(), "injected!" + mAppComponentA);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
