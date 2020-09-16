package com.donfyy.skin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.donfyy.skinlib.SkinManager;

import androidx.annotation.Nullable;

public class SkinActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
    }

    public void change(View view) {
        SkinManager.loadSkin("/data/data/com.donfyy.skin/skin/skin-debug.apk");
    }

    public void restore(View view) {
        SkinManager.loadSkin(null);
    }
}
