package com.donfyy.viewexample.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ImageUtils
import com.donfyy.viewexample.R

class BitmapDrawView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : View(context, attrs, defStyleAttr, defStyleRes) {
    private var mClipPath: Path = Path()
    private var mPaint: Paint = Paint()
    private var mBitmapEx: Bitmap
    private var mBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.scene)
    private var mWidth = 0
    private var mHeight = 0
    private var mDensity = 0f

    init {
        mPaint.isAntiAlias = true
        mBitmap = ImageUtils.compressByScale(mBitmap, ConvertUtils.dp2px(100f), ConvertUtils.dp2px(100f), true)
        mWidth = mBitmap.width
        mHeight = mBitmap.height
        mBitmapEx = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        var displayMetrics = DisplayMetrics()
        displayMetrics = context.resources.displayMetrics
        mDensity = displayMetrics.density
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var x: Float
        var y: Float

//        canvas.drawBitmap(mBitmap, 50 * mDensity, 20 * mDensity, mPaint);
        // 通过xfermodes
        x = width / 2 - mBitmap.width / 2.toFloat()
        y = 140 * mDensity
        mBitmapEx = processRoundBitmap(mBitmap)
        canvas.drawBitmap(mBitmapEx, x, y, mPaint)

        y = 260 * mDensity
        // 通过剪裁
        canvas.save()
//        canvas.clipRect(x, y, x + mWidth, y + mHeight)
        mClipPath.addOval(RectF(x, y, x + mWidth, y + mHeight), Path.Direction.CCW)
        canvas.clipPath(mClipPath)
//        canvas.clipPath(mClipPath, Region.Op.DIFFERENCE)
        canvas.drawBitmap(mBitmap, x, y, mPaint)
//        canvas.drawColor(Color.WHITE)
        canvas.restore()
        // 第三种方式通过着色器 BitmapShader
        mPaint.shader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        canvas.drawOval(0f, 0f, 1f, 1f, mPaint)
    }

    private fun processRoundBitmap(bitmap: Bitmap): Bitmap {
        val x = 0f
        val y = 0f
        val paint = Paint().apply { isAntiAlias = true }
        val mXfermodeSrcIn = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        val bitmapEx: Bitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        // 注意注意画笔的透明值
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        Canvas(bitmapEx).drawOval(RectF(x, y, (x + 2 * (bitmapEx.height / 2)), (y + 2 * (bitmapEx.height / 2))), paint)
        paint.xfermode = mXfermodeSrcIn
        Canvas(bitmapEx).drawBitmap(bitmap, x, y, paint)
        return bitmapEx
    }
}