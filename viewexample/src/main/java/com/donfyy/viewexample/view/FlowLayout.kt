package com.donfyy.viewexample.view

import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller
import kotlin.math.abs
import kotlin.math.max

class FlowLayout @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    private val rowList = mutableListOf<List<View>>()
    private val rowHeightList = mutableListOf<Int>()
    private var contentHeight = 0
    private var mTouchSlop = 0
    private var mMinimumVelocity = 0
    private var mMaximumVelocity = 0

    init {
        val configuration = ViewConfiguration.get(getContext())
        mTouchSlop = configuration.scaledTouchSlop
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var rowWidth = 0
        var rowViews = mutableListOf<View>()
        var flowLayoutHeight = 0
        var flowLayoutWidth = 0
        var rowHeight = 0
        rowList.clear()
        rowHeightList.clear()
        for (i in 0 until childCount) {

            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)

            if (rowWidth + child.measuredWidth > widthSize) {
                //换行
                rowList.add(rowViews)
                rowHeightList.add(rowHeight)
                flowLayoutWidth = max(flowLayoutWidth, rowWidth)
                flowLayoutHeight += rowHeight
                //清除行的数据
                rowViews = mutableListOf()
                rowWidth = 0
                rowHeight = 0
            }

            rowWidth += child.measuredWidth
            rowViews.add(child)
            rowHeight = max(rowHeight, child.measuredHeight)
        }
        if (rowViews.isNotEmpty()) {
            rowList.add(rowViews)
            flowLayoutHeight += rowHeight
            rowHeightList.add(rowHeight)
        }

        contentHeight = flowLayoutHeight
        setMeasuredDimension(
                View.resolveSize(flowLayoutWidth, widthMeasureSpec),
                View.resolveSize(flowLayoutHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        var rowTop = 0
        for ((index, row) in rowList.withIndex()) {
            var childLeft = 0
            for (child in row) {
                child.layout(
                        childLeft, rowTop, childLeft + child.measuredWidth, rowTop + child.measuredHeight
                )
                childLeft += child.measuredWidth
            }
            rowTop += rowHeightList[index]
        }
    }


    private var downPoint = PointF()
    private var touchSlop = ViewConfiguration.getTouchSlop()
    private var isScroll = false;
    private var startScrollPoint = Point()
    private val scroller = OverScroller(context)
    private var velocityTracker: VelocityTracker? = null
    private fun initVelocityTrackerIfNotExists() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (contentHeight <= measuredHeight) {
            return super.onTouchEvent(event)
        }
        initVelocityTrackerIfNotExists()
        velocityTracker?.addMovement(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downPoint.set(event.x, event.y)
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
//                    scrollTo(0, (downPoint.y - event.y).toInt() + startScrollPoint.y)
                // replace scroll to with scroller
//                    scroller.startScroll(0, 0, 0, ((downPoint.y - event.y).toInt()))
                scroller.startScroll(0, scroller.finalY, 0, ((downPoint.y - event.y).toInt()))
                invalidate()
                downPoint.set(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker?.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val yVelocity = velocityTracker?.yVelocity
                if (yVelocity != null) {
                    if (abs(yVelocity) > mMinimumVelocity) {
                        scroller.fling(
                                scrollX, scrollY, 0, -(yVelocity).toInt(), 0, 0, 0, max(0, contentHeight - measuredHeight), 0, measuredHeight / 4
                        )
                        postInvalidateOnAnimation()
                    } else if (scroller.springBack(scrollX, scrollY, 0, 0, 0, contentHeight - measuredHeight)) {
                        postInvalidateOnAnimation()
                    }
                }
            }
            else -> {
            }
        }


        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var isIntercept = false
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                downPoint.set(ev.x, ev.y)
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = ev.y - downPoint.y
                if (abs(dy) > abs(ev.x - downPoint.x) && abs(dy) > touchSlop) {
                    isIntercept = true
                }
            }
            MotionEvent.ACTION_UP -> {

            }
            else -> {
            }
        }
        return isIntercept
    }

    val point = Point()
    override fun computeScroll() {
        super.computeScroll()


        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
//            scrollBy(scroller.currX - point.x, scroller.currY - point.y)
            postInvalidate()
//            point.set(scroller.currX, scroller.currY)
        }
    }
}