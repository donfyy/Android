package com.example.rxjava2

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("rom version:" + Util.getRomVersion())
        Log.e(MainActivity::class.java.name, "" + Environment.getExternalStorageDirectory() )
        Log.e(MainActivity::class.java.name, "" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) )
//        Observable.create<Int> {
////            Thread.sleep(5000)
////            it.onError(RuntimeException("hhh"))
//            throw RuntimeException("hhh")
//        }.timeout(3, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.computation())
//                .subscribe({
//                    Log.i(MainActivity::javaClass.name, "integer:${it}")
//                }, {
//                    Log.e(MainActivity::javaClass.name, "error xxxxxxxxxxxxxx", it)
//                })


//        var    uriString = Uri("ttfile://temp/1629172717075.jpg").toString()

    }
}