package com.donfyy.skinlib

import android.app.Application

object SkinManager {
    @JvmStatic
    fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(ActivityLifecycleCallback())
    }
}