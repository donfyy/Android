package com.donfyy.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

object Utils {
    @JvmStatic
    fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                Resources.getSystem().displayMetrics).toInt()
    }

    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    @JvmStatic
    // 解析位图，并将位图缩放至width。width的单位是像素
    fun getBitmapByWidth(resources: Resources?, resId: Int, width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, resId, options)
    }
}