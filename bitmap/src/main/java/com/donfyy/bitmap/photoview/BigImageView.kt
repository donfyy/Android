package com.donfyy.bitmap.photoview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Scroller
import com.blankj.utilcode.util.LogUtils
import java.io.IOException
import java.io.InputStream

/**
 * 长图默认填满全部屏幕，默认宽度填充
 * 首先算出要解析的矩形区域的宽和高是确定的
 */
class BigImageView
@JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr), GestureDetector.OnGestureListener, OnTouchListener {
    // 保存要解析的图片区域
    private var mRect: Rect = Rect()
    private var mOptions: BitmapFactory.Options = BitmapFactory.Options()
    private var mImageWidth = 0
    private var mImageHeight = 0
    private var mBitmapRegionDecoder: BitmapRegionDecoder? = null
    private val mGestureDetector: GestureDetector = GestureDetector(context, this)
    private val mScroller: Scroller = Scroller(context)
    var bitmap: Bitmap? = null
    private var mViewHeight = 0
    private var mViewWidth = 0
    private var mScale = 0f

    fun setImage(inputStream: InputStream) {
        mOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, mOptions)
        mImageWidth = mOptions.outWidth
        mImageHeight = mOptions.outHeight
        mOptions.inMutable = true
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565
        mOptions.inJustDecodeBounds = false
        try {
            // false 不共享 图片源
            mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewHeight = measuredHeight
        mViewWidth = measuredWidth
        if (mBitmapRegionDecoder == null) {
            return
        }

        // 计算出要解析的矩形区域
        mRect.left = 0
        mRect.top = 0
        mRect.right = mImageWidth

        // 缩放因子
        mScale = mViewWidth / mImageWidth.toFloat()
        // x * mscale = mViewHeight
        mRect.bottom = (mViewHeight / mScale).toInt()

        // 第一种方式优化
        mOptions.inSampleSize = calculateInSampleSize(mImageWidth, mImageHeight, mViewWidth, mViewHeight);

        // 第二种方式优化
//        val temp = 1.0f / mScale
//        if (temp > 1) {
//            mOptions.inSampleSize = 2.0.pow(temp.toInt().toDouble()).toInt()
//        } else {
//            mOptions.inSampleSize = 1
//        }
        LogUtils.d("============缩放后========= \n " +
                "inSampleSize = " + mOptions.inSampleSize + "" +
                "\nmScale = $mScale " +
                "\n图片宽 = $mImageWidth,高 = $mImageHeight " +
                "\nview 宽 = $mViewWidth,高 = $mViewHeight")
    }

    private val bitmapMatrix = Matrix()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mBitmapRegionDecoder?.let {
            // 复用Bitmap
            mOptions.inBitmap = bitmap
            bitmap = it.decodeRegion(mRect, mOptions).apply {
                LogUtils.d("图片大小 $byteCount")
                // 没有优化：44338752 1.优化：2770200，2优化：692064
                // 没有优化：50388480 1.优化：3149280，2优化：787320
//                bitmapMatrix.setScale(mScale, mScale)
                bitmapMatrix.setScale(mScale * mOptions.inSampleSize, mScale * mOptions.inSampleSize);
                canvas.drawBitmap(this, bitmapMatrix, null)
            }
        }
    }

    override fun onDown(e: MotionEvent): Boolean {
        // 如果滑动还没有停止 强制停止
        if (!mScroller.isFinished) {
            mScroller.forceFinished(true)
        }
        // 继续接收后续事件
        return true
    }

    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {}
    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        // 改变加载图片的区域
        mRect.offset(0, distanceY.toInt())
        // bottom大于图片高了， 或者 top小于0了
        if (mRect.bottom > mImageHeight) {
            mRect.bottom = mImageHeight
            mRect.top = mImageHeight - (mViewHeight / mScale).toInt()
        }
        if (mRect.top < 0) {
            mRect.top = 0
            mRect.bottom = (mViewHeight / mScale).toInt()
        }
        // 重绘
        invalidate()
        return false
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        /**
         * startX: 滑动开始的x坐标
         * velocityX: 以每秒像素为单位测量的初始速度
         * minX: x方向滚动的最小值
         * maxX: x方向滚动的最大值
         */
        mScroller.fling(0, mRect.top, 0, (-velocityY).toInt(), 0, 0,
                0, mImageHeight - (mViewHeight / mScale).toInt())
        return false
    }

    /**
     * 获取计算结果并且重绘
     */
    override fun computeScroll() {
        // 已经计算结束 return
        if (mScroller.isFinished) {
            return
        }
        // true 表示当前动画未结束
        if (mScroller.computeScrollOffset()) {
            mRect.top = mScroller.currY
            mRect.bottom = mRect.top + (mViewHeight / mScale).toInt()
            invalidate()
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        // 事件交给手势处理
        return mGestureDetector.onTouchEvent(event)
    }

    companion object {
        /**
         * @param w    图片宽
         * @param h    图片高
         * @param maxW View 宽
         * @param maxH View 高
         * @return
         */
        private fun calculateInSampleSize(w: Int, h: Int, maxW: Int, maxH: Int): Int {
            var inSampleSize = 1
            if (w > maxW && h > maxH) {
                inSampleSize = 2
                while (w / inSampleSize > maxW && h / inSampleSize > maxH) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize
        }
    }

    init {
        // 将触摸事件交给手势处理
        setOnTouchListener(this)
        // 滑动帮助
    }
}