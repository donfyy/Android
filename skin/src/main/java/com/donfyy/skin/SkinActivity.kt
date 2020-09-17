package com.donfyy.skin

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.donfyy.skinlib.SkinManager.loadSkin

class SkinActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin)
    }

    fun change(view: View?) {
        loadSkin("/data/data/com.donfyy.skin/skin/skin2-debug.apk")
        //        SkinManager.loadSkin("/data/data/com.donfyy.skin/skin/skin-debug.apk");
    }

    fun restore(view: View?) {
        loadSkin(null)
    }
}