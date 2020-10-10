package com.donfyy.bitmap

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils

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
}
