package com.donfyy.viewpager

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.donfyy.viewpager.lazyloading.LazyMainActivity
import com.donfyy.viewpager.lazyloading.lazy2.LazyMain2Activity
import com.donfyy.viewpager.lazyloading.lazy3.LazyMain3Activity
import com.donfyy.viewpager.wrap.WrapActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onEnterMain1(view: View) {
        startActivity(Intent(this, LazyMainActivity::class.java))
    }
    fun onEnterMain2(view: View) {
        startActivity(Intent(this, LazyMain2Activity::class.java))
    }
    fun onEnterMain3(view: View) {
        startActivity(Intent(this, LazyMain3Activity::class.java))
    }
    fun onEnterWrapMain(view: View) {
        startActivity(Intent(this, WrapActivity::class.java))
    }
}