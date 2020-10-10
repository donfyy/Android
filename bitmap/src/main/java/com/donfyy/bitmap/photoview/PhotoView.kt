package com.donfyy.bitmap.photoview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
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
import com.donfyy.util.Calculator
import com.donfyy.util.Utils

/**
 * 整个PhotoView应该实现的功能
 * 1.双击放大与缩小
 * 2.使用手指放大与缩小
 * 3.使用手指平移
 * 基于Canvas的scale方法与translate方法进行缩放和平移操作，
 * 基于drawBitmap方法绘制Bitmap。通过GestureDetector来监听双击和滑动事件。
 * 通过ScaleGestureDetector来监听缩放事件。
 * 在缩放时需要考虑滑动距离，在滑动时考虑缩放。
 * 使用Canvas绘制的Bitmap会应用Canvas的Matrix。
 * 所以此处Bitmap绘制的位置并未发生改变。
 * 以画布的中心点放大与缩小。
 * 难点在于不同缩放系数下的滑动距离的处理。
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
    private val DEFAULT_DURATION = 200L
    private var gestureDetector: GestureDetector
    private val boarder = Rect()
    private val boarderPaint = Paint()
    private val scaleAnimator: ObjectAnimator by lazy(LazyThreadSafetyMode.NONE) {
        ObjectAnimator.ofFloat(this, "currentScale", 0f)
    }

    // 在maxScale缩放级别下，x轴与y轴的滑动距离
    private var offsetX = 0f
    private var offsetY = 0f
    private var overScroller: OverScroller
    private var scaleGestureDetector: ScaleGestureDetector
    private val bitmapMatrix = Matrix()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let {
            val scaleFaction = (currScale - minScale) / (maxScale - minScale)
            // 当前放大比例为 small 时，scaleFaction = 0，不偏移
//            canvas.translate(offsetX * scaleFaction, offsetY * scaleFaction);
//            canvas.scale(currScale, currScale, width / 2f, height / 2f)
////            canvas.drawRect(boarder, boarderPaint)
//            canvas.drawBitmap(it, originalLeft, originalTop, paint)
            // 对画布进行操作，稍微有点难以理解。。。画布的操作正好是反着来的
            bitmapMatrix.reset()
            bitmapMatrix.postScale(currScale, currScale, it.width / 2f, it.height / 2f)
            bitmapMatrix.postTranslate(originalLeft, originalTop)
            bitmapMatrix.postTranslate(offsetX * scaleFaction, offsetY * scaleFaction)
            canvas.drawBitmap(it, bitmapMatrix, paint)
        }
    }

    private fun isHorizontal(): Boolean {
        bitmap?.let {
            return it.width.toFloat() / it.height > width.toFloat() / height
        }
        return false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        LogUtils.d("w = $w h = $h oldw = $oldw oldh = $oldh")
        bitmap?.let {
            originalLeft = (w - it.width) / 2f
            originalTop = (h - it.height) / 2f
            if (isHorizontal()) {
                // 宽全屏
                minScale = w.toFloat() / it.width
                maxScale = h.toFloat() / it.height * OVER_SCALE_FACTOR
            } else {
                // 高全屏
                minScale = h.toFloat() / it.height
                maxScale = w.toFloat() / it.width * OVER_SCALE_FACTOR
            }
            boarder.set((w - it.width) / 2,
                    (h - it.height) / 2,
                    (w + it.width) / 2,
                    (h + it.height) / 2)
            currScale = minScale
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            if (!overScroller.isOverScrolled) {
                overScroller.abortAnimation()
            }
        }
        // 响应事件以双指缩放优先
        var result = scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            result = gestureDetector.onTouchEvent(event)
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
            if (currScale > minScale && !scaleGestureDetector.isInProgress) {
                offsetX -= distanceX / (currScale - minScale) * (maxScale - minScale)
                offsetY -= distanceY / (currScale - minScale) * (maxScale - minScale)
                fixOffsets(currScale)
                invalidate()
            }
//                        }
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
            if (currScale > minScale) {
                bitmap?.let {
                    val dw = maxOf(0f, (it.width * currScale - width) / 2f).toInt()
                    val dh = maxOf(0f, (it.height * currScale - height) / 2f).toInt()
                    val x = (offsetX * (currScale - minScale) / (maxScale - minScale)).toInt()
                    val y = (offsetY * (currScale - minScale) / (maxScale - minScale)).toInt()
                    overScroller.fling(
                            x, y, velocityX.toInt(), velocityY.toInt(), -dw, dw, -dh, dh, 300, 300
                    )
                }
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
            if (currScale == minScale) {
                val distanceX = e.x - width / 2f
                val distanceY = e.y - height / 2f
                offsetX = distanceX - Calculator.getScaledDistance(distanceX, minScale, maxScale)
                offsetY = distanceY - Calculator.getScaledDistance(distanceY, minScale, maxScale)
                fixOffsets(maxScale)
                scaleAnimator.apply {
                    setFloatValues(minScale, maxScale)
                    duration = DEFAULT_DURATION
                    start()
                }
            } else {
                scaleAnimator.apply {
                    setFloatValues(currScale, minScale)
                    duration = DEFAULT_DURATION
//                    duration = (DEFAULT_DURATION * (currScale - minScale) / (maxScale - minScale)).toLong()
                    start()
                }
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
                offsetX = overScroller.currX * (maxScale - minScale) / (currScale - minScale)
                offsetY = overScroller.currY * (maxScale - minScale) / (currScale - minScale)
                invalidate()
                // 下一帧动画的时候执行
                postOnAnimation(this)
            }
        }
    }

    // offset是在缩放系数为上界时的滑动距离
    private fun fixOffsets(currScale: Float) {
        bitmap?.let {
            val deltaWidth = it.width * currScale - width
            val maxDeltaWidth = it.width * maxScale - width
            offsetX = if (deltaWidth > 0) {
                if (isHorizontal()) {
                    // 宽全屏，滑动距离的上下界是固定的
                    maxOf(minOf(offsetX, maxDeltaWidth / 2f), -maxDeltaWidth / 2f)
                } else {
                    // 高全屏下，滑动距离的上下界需要随着缩放系数的变化而变化
                    maxOf(minOf(offsetX, maxDeltaWidth / 2f), -maxDeltaWidth / 2f)
                }
            } else {
                0f
            }
            val deltaHeight = it.height * currScale - height
            val maxDeltaHeight = it.height * maxScale - height
            offsetY = if (deltaHeight > 0) {
                if (isHorizontal()) {
                    // 宽全屏下，垂直方向上的滑动距离上下限随着缩放系数的变化而变化
                    maxOf(minOf(offsetY, deltaHeight / 2f), -deltaHeight / 2f)
                } else {
                    // 高全屏下，垂直方向上的滑动距离上下限是固定的
                    maxOf(minOf(offsetY, maxDeltaHeight / 2f), -maxDeltaHeight / 2f)
                }
            } else {
                0f
            }
        }
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

        // 返回值为false表示不更新求得的直径
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // 缩放因子
            val scale = initScale * detector.scaleFactor
            if (scale in minScale..maxScale) {
                currScale = initScale * detector.scaleFactor
                fixOffsets(currScale)
                invalidate()
            }

            return false
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            initScale = currScale
            LogUtils.d("currScale = $currScale  offsetX = $offsetX  offsetY = $offsetY")
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            LogUtils.d("...")
        }
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
        boarderPaint.color = Color.RED
        boarderPaint.style = Paint.Style.STROKE
        boarderPaint.strokeWidth = Utils.dp2px(1).toFloat()
    }
}