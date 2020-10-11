package com.donfyy.bitmap.cache

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.donfyy.bitmap.R

class CacheActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cache)
        initView()
        decodeBitmap()
        ImageCache.init(this, Environment.getExternalStorageDirectory().toString() + "/bitmap")
    }

    /**
     * 初始化 recyclerView
     */
    private fun initView() {
        val rv = findViewById<RecyclerView>(R.id.rv)
        val linearLayoutManager = LinearLayoutManager(this)
        rv.layoutManager = linearLayoutManager
        val bitmapAdapter = BitmapAdapter(this)
        rv.adapter = bitmapAdapter
    }

    /**
     * 解析图片
     */
    private fun decodeBitmap() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_mv)
        Log.e("leo", "decodeBitmap: " + bitmap.width + "X" + bitmap.height + "x"
                + bitmap.config + ",内存总大小" + bitmap.byteCount)
        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.icon_mv_p)
        Log.e("leo", "decodeBitmap: " + bitmap1.width + "X" + bitmap1.height + "x"
                + bitmap1.config + ",内存总大小" + bitmap1.byteCount)
        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.icon_mv_w)
        Log.e("leo", "decodeBitmap: " + bitmap2.width + "X" + bitmap2.height + "x"
                + bitmap2.config + ",内存总大小" + bitmap2.byteCount)
    }
}