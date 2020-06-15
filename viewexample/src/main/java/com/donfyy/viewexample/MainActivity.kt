package com.donfyy.viewexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
