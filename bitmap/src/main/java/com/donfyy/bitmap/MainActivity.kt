package com.donfyy.bitmap

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.LruCache
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.values
import com.blankj.utilcode.util.LogUtils
import com.donfyy.bitmap.cache.CacheActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resources.displayMetrics.apply {
            LogUtils.d("\n " +
                    "density = $density \n " +
                    "densityDpi = $densityDpi \n " +
                    "xdpi = $xdpi \n " +
                    "ydpi = $ydpi \n " +
                    "widthPixels = $widthPixels \n " +
                    "heightPixels = $heightPixels \n " +
                    "scaledDensity = $scaledDensity"
            )
        }
        val typedValue = TypedValue()
        val ist = resources.openRawResource(R.drawable.photo, typedValue)
        LogUtils.d("density = ${typedValue.density}")
        ist.close()
        val image = BitmapFactory.decodeResource(resources, R.drawable.photo)
        LogUtils.d("\n byteCount = ${image.byteCount} \n " +
                "allocationByteCount = ${image.allocationByteCount} \n " +
                "width = ${image.width}\n " +
                "height = ${image.height}")
        image.recycle()

        val cache = object : LruCache<Int, Int>(3) {
            override fun entryRemoved(evicted: Boolean, key: Int?, oldValue: Int?, newValue: Int?) {
                LogUtils.d(oldValue)
            }
        }
        cache.put(1, 1)
        cache.put(2, 2)
        cache.put(3, 3)
        cache.put(4, 4)

        val m1 = Matrix().apply { setValues(floatArrayOf(1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f)) }
        val m2 = Matrix().apply { setValues(floatArrayOf(1f, 2f, 3f, 1f, 2f, 3f, 1f, 2f, 3f)) }
        val m3 = Matrix().apply { setValues(floatArrayOf(1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f)) }
        val m4 = Matrix().apply { setValues(floatArrayOf(1f, 2f, 3f, 1f, 2f, 3f, 1f, 2f, 3f)) }

        m1.postConcat(m2) // m2 * m1
        LogUtils.d(m1.toString())
        m3.preConcat(m4) // m3 * m4
        LogUtils.d(m3.toString())
    }

    private fun showActivity(clazz: Class<out AppCompatActivity>) {
        startActivity(Intent(this, clazz))
    }

    fun onShowPhoto(view: View) {
        showActivity(PhotoActivity::class.java)
    }

    fun onShowMultiTouch(view: View) {
        showActivity(MultiTouchActivity::class.java)
    }

    fun onShowBigImage(view: View) {
        showActivity(BigImageActivity::class.java)
    }

    fun onShowCache(view: View) {
        showActivity(CacheActivity::class.java)
    }
}
