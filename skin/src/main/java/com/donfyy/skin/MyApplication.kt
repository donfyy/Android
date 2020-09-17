package com.donfyy.skin

import android.app.Application
import com.donfyy.skinlib.SkinManager.init

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        init(this)
    }
}