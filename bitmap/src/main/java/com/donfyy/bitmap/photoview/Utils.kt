package com.donfyy.bitmap.photoview

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.donfyy.bitmap.R

object Utils {

    @JvmStatic
    // 解析位图，并将位图缩放至width。width的单位是像素
    fun getPhoto(res: Resources?, width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, R.drawable.photo, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(res, R.drawable.photo, options)
    }
}