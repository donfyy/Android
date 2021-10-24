package com.example.target30

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e(MainActivity::class.java.name, "" + Environment.getExternalStorageDirectory() )
        Log.e(MainActivity::class.java.name, "" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) )
    }
}