package com.donfyy.skinlib

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat
import com.blankj.utilcode.util.LogUtils
import com.donfyy.skinlib.utils.ThemeUtils
import java.lang.reflect.Field

class ActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {
    companion object {
        var sLayoutInflaterFactory2Field: Field? = null
    }

    val map = HashMap<Activity, LayoutInflaterFactory>()
    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        val factory = map.remove(activity)
        SkinManager.deleteObserver(factory)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        try {
            ThemeUtils.updateStatusBar(activity)
            val factory = LayoutInflaterFactory(activity)
            map[activity] = factory
            if (sLayoutInflaterFactory2Field == null) {
                sLayoutInflaterFactory2Field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            }
            sLayoutInflaterFactory2Field?.let {
                it.isAccessible = true
                LogUtils.d("mFactorySet = ${it.get(activity.layoutInflater)} " +
                        "layoutinfalter = ${activity.layoutInflater} " +
                        "baseContext = ${activity.baseContext} " +
                        "factory2 = ${activity.layoutInflater.factory2}")
                it.set(activity.layoutInflater, false)
            }
            LayoutInflaterCompat.setFactory2(activity.layoutInflater, factory)
            SkinManager.addObserver(factory)
        } catch (e: Exception) {
            LogUtils.e("forceSetFactory2 Could not find field 'mFactorySet' on class "
                    + LayoutInflater::class.java.name
                    + "; inflation may have unexpected results.", e)
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }
}