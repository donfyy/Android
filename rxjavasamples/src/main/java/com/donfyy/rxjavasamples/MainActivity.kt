package com.donfyy.rxjavasamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.Flowable

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun hi(view: View) {
        Flowable.just("Hi~").subscribe { Snackbar.make(window.decorView, it, Snackbar.LENGTH_LONG).show() }
    }
}
