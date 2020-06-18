package com.donfyy.viewexample

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.core.app.ActivityManagerCompat

class MainActivity : AppCompatActivity() {

    //    val array  = ByteArray(1024 * 1024 * 550)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val runningAppProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(runningAppProcessInfo)
        println(runningAppProcessInfo)
        val systemService = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        Log.e("memory", systemService.memoryClass.toString() + "m")

        Log.e("large heap memory", systemService.largeMemoryClass.toString() + "m")

        var name = window.javaClass.name
        val window = window
        var javaClass: Class<Any> = window.javaClass
        Log.e("window name", javaClass.name)
        var superclass = javaClass.superclass
        while (superclass != null) {
            Log.e("window name", superclass.name)
            superclass = superclass.superclass
        }
        /*com.android.internal.policy.HwPhoneWindow
        com.android.internal.policy.PhoneWindow
        com.android.internal.policy.AbsWindow
        android.view.Window
        java.lang.Object
        androidx.appcompat.app.AppCompatActivity
*/
        Log.e("window name", this.javaClass.superclass?.name)

    }

    fun onShowVectorDrawableExample(view: View) {
        showActivity(VectorDrawableActivity::class.java)
    }

    private fun showActivity(clazz: Class<out AppCompatActivity>) {
        startActivity(Intent(this, clazz))
    }

    fun onShowLottieExample(view: View) {
        showActivity(LottieActivity::class.java)
    }
}
