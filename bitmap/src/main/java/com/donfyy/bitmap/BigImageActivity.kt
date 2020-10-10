package com.donfyy.bitmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.donfyy.bitmap.photoview.BigImageView
import java.io.IOException
import java.io.InputStream


class BigImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_image)
        val bigImageView = findViewById<BigImageView>(R.id.iv_big)
        var inputStream: InputStream? = null

        try {
            inputStream = assets.open("big.png");
//            inputStream = assets.open("world.jpg")
            bigImageView.setImage(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}