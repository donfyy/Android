package com.donfyy.bitmap.photoview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.widget.OverScroller
import com.blankj.utilcode.util.LogUtils
import com.donfyy.bitmap.R
import com.donfyy.util.Utils

/**
 * 1.双击放大与缩小
 * 2.放大与缩小
 * 3.平移
 */
class PhotoView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var bitmap: Bitmap? = null
    private var paint: Paint
    private var originalLeft = 0f
    private var originalTop = 0f
    private var minScale = 0f
    private var maxScale = 0f
    private var currScale = 0f
    private val OVER_SCALE_FACTOR = 1.5f
    private var gestureDetector: GestureDetector? = null
    private var isEnlarge = false
    private val scaleAnimator: ObjectAnimator by lazy(LazyThreadSafetyMode.NONE) {
        ObjectAnimator.ofFloat(this, "currentScale", 0f)
    }

    // 在maxScale缩放级别下，x轴与y轴的滑动距离
    private var offsetX = 0f
    private var offsetY = 0f
    private var overScroller: OverScroller
    private var scaleGestureDetector: ScaleGestureDetector

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let {
            val scaleFaction = (currScale - minScale) / (maxScale - minScale)
            // 当前放大比例为 small 时，scaleFaction = 0，不偏移
            canvas.translate(offsetX * scaleFaction, offsetY * scaleFaction);
            canvas.scale(currScale, currScale, width / 2f, height / 2f)
//        LogUtils.d("density = " + canvas.getDensity());
            canvas.drawBitmap(it, originalLeft, originalTop, paint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        LogUtils.d("w = $w h = $h oldw = $oldw oldh = $oldh")
        bitmap?.let {
            originalLeft = (w - it.width) / 2f
            originalTop = (h - it.height) / 2f
            if (it.width.toFloat() / it.height > w.toFloat() / h) {
                // 宽全屏
                minScale = w.toFloat() / it.width
                maxScale = h.toFloat() / it.height * OVER_SCALE_FACTOR
            } else {
                // 高全屏
                minScale = h.toFloat() / it.height
                maxScale = w.toFloat() / it.width * OVER_SCALE_FACTOR
            }
            currScale = minScale
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            if (!overScroller.isOverScrolled && !overScroller.isFinished) {
                overScroller.abortAnimation()
            }
        }
        // 响应事件以双指缩放优先
        var result = scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            result = gestureDetector!!.onTouchEvent(event)
        }
        return result
    }

    internal inner class PhotoGestureDetector : SimpleOnGestureListener() {
        // up 触发，单击或者双击的第一次会触发 --- up时，如果不是双击的第二次点击，不是长按，则触发
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return super.onSingleTapUp(e)
        }

        // 长按 -- 默认300ms触发
        override fun onLongPress(e: MotionEvent) {
            super.onLongPress(e)
        }

        /**
         * 滚动 -- move
         *
         * @param e1        手指按下
         * @param e2        当前的
         * @param distanceX 旧位置 - 新位置
         * @param distanceY
         * @return
         */
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
//            if (isEnlarge) {
            offsetX -= distanceX
            offsetY -= distanceY
            fixOffsets()
            invalidate()
            //            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        /**
         * up 惯性滑动 -- 大于50dp/s
         *
         * @param velocityX x轴方向运动速度（像素/s）
         * @param velocityY
         * @return
         */
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (isEnlarge) {
                overScroller.fling(offsetX.toInt(), offsetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                        (-(bitmap!!.width * maxScale - width)).toInt() / 2,
                        (bitmap!!.width * maxScale - width).toInt() / 2,
                        (-(bitmap!!.height * maxScale - height)).toInt() / 2,
                        (bitmap!!.height * maxScale - height).toInt() / 2,
                        300, 300)
                postOnAnimation(FlingRunner())
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        // 延时100ms触发 -- 处理点击效果
        override fun onShowPress(e: MotionEvent) {
            super.onShowPress(e)
        }

        // 只需要关注 onDown 的返回值即可
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        // 双击的第二次点击down时触发。双击的触发时间 -- 40ms -- 300ms
        override fun onDoubleTap(e: MotionEvent): Boolean {
            isEnlarge = !isEnlarge
            if (isEnlarge) {
//                offsetX = e.x - width / 2f -
//                        (e.x - width / 2f) * bigScale / smallScale
//                offsetY = e.y - height / 2f -
//                        (e.y - height / 2f) * bigScale / smallScale
                fixOffsets()
                scaleAnimation.start()
            } else {
                scaleAnimation.reverse()
            }
            return super.onDoubleTap(e)
        }

        // 双击的第二次down、move、up 都触发
        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            return super.onDoubleTapEvent(e)
        }

        // 单击按下时触发，双击时不触发，down，up时都可能触发
        // 延时300ms触发TAP事件
        // 300ms以内抬手 -- 才会触发TAP -- onSingleTapConfirmed
        // 300ms 以后抬手 --- 不是双击不是长按，则触发
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return super.onSingleTapConfirmed(e)
        }
    }

    internal inner class FlingRunner : Runnable {
        override fun run() {
            if (overScroller.computeScrollOffset()) {
                offsetX = overScroller.currX.toFloat()
                offsetY = overScroller.currY.toFloat()
                invalidate()
                // 下一帧动画的时候执行
                postOnAnimation(this)
            }
        }
    }

    private fun fixOffsets() {
//        offsetX = Math.min(offsetX, (bitmap.getWidth() * currentScale - getWidth()) / 2);
//        offsetX = Math.max(offsetX, -(bitmap.getWidth() * currentScale - getWidth()) / 2);
//        offsetY = Math.min(offsetY, (bitmap.getHeight() * currentScale - getHeight()) / 2);
//        offsetY = Math.max(offsetY, -(bitmap.getHeight() * currentScale - getHeight()) / 2);
        offsetX = Math.min(offsetX, (bitmap!!.width * maxScale - width) / 2)
        offsetX = Math.max(offsetX, -(bitmap!!.width * maxScale - width) / 2)
        offsetY = Math.min(offsetY, (bitmap!!.height * maxScale - height) / 2)
        offsetY = Math.max(offsetY, -(bitmap!!.height * maxScale - height) / 2)
    }

    private val scaleAnimation: ObjectAnimator
        get() {
            return scaleAnimator.apply { setFloatValues(minScale, maxScale) }
        }

    fun getCurrentScale(): Float {
        return currScale
    }

    fun setCurrentScale(currentScale: Float) {
        this.currScale = currentScale
        invalidate()
    }

    internal inner class PhotoScaleGestureListener : OnScaleGestureListener {
        var initScale = 0f
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (currScale > minScale && !isEnlarge
                    || currScale == minScale && !isEnlarge) {
                isEnlarge = !isEnlarge
            }
            // 缩放因子
            currScale = initScale * detector.scaleFactor
            invalidate()
            return false
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            initScale = currScale
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {}
    }

    companion object {
        private val IMAGE_WIDTH = Utils.dp2px(300)
    }

    init {
        bitmap = Utils.getBitmapByWidth(resources, R.drawable.photo, IMAGE_WIDTH)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        gestureDetector = GestureDetector(context, PhotoGestureDetector())
        overScroller = OverScroller(context)
        scaleGestureDetector = ScaleGestureDetector(context, PhotoScaleGestureListener())
    }
}