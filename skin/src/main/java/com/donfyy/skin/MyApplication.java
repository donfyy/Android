package com.donfyy.skin;

import android.app.Application;

import com.donfyy.skinlib.SkinManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
