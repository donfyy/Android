package com.donfyy.e_conflictfixcases.nestedscroll

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.LogUtils
import com.donfyy.e_conflictfixcases.nestedscroll.utils.FlingHelper

class NestedScrollLayout
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0)
    : NestedScrollView(context, attrs, defStyleAttr) {
    private lateinit var headerView: View
    private lateinit var contentView: ViewGroup
    private var flingHelper: FlingHelper = FlingHelper(this.context)
    private val scrollChildView = ScrollingChild()

    private var isInFling = false
    private var startScrollY = 0
    private var startVelocityY = 0

    init {
        // 监听自身的滑动，当用户从headerView进行滑动时，使得多余的滑动距离委托给子View进行处理。
        setOnScrollChangeListener { _, _, _, _, _ ->
            // 自身滑动到底部
            if (scrollY == headerView.measuredHeight) {
                // 如果惯性滑动是由自身触发的，找到嵌套滑动的子View把惯性滑动分发给它们
                if (isInFling) {
                    LogUtils.d("isInFling")
                    isInFling = false
                    dispatchChildFling()
                }
            }
        }
    }

    private fun dispatchChildFling() {
        if (startVelocityY != 0) {
            // 计算自己已经滑动的距离
            val consumedDy = scrollY - startScrollY
            // 计算可以滑动的总距离
            val totalDy = flingHelper.getSplineFlingDistance(startVelocityY)
            if (totalDy > consumedDy) {
                // 将剩余的滑动距离转换成速度分发给嵌套滑动子View
                flingChild(flingHelper.getVelocityByDistance(totalDy - consumedDy))
            }
            startVelocityY = 0
        }
    }

    private fun flingChild(velY: Int) {
        scrollChildView.clear()
        // 找到嵌套滑动子View
        if (fillChildScrollingView(contentView)) {
            // 把惯性滑动分发给它们
            scrollChildView.recyclerView?.fling(0, velY)
            scrollChildView.nestedScrollView?.fling(velY)
        }
    }

    // 处理自身发起的惯性滑动
    override fun fling(velocityY: Int) {
        super.fling(velocityY)
        // 记录是否在惯性滑动
        isInFling = true
        // 记录下惯性滑动开始时的滑动位置
        startScrollY = scrollY
        // 记录下惯性滑动开始时的速度
        startVelocityY = velocityY
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        headerView = (getChildAt(0) as ViewGroup).getChildAt(0)
        contentView = (getChildAt(0) as ViewGroup).getChildAt(1) as ViewGroup
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 调整contentView的高度为父容器高度，使之填充布局，避免父容器滑动后出现空白
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val lp = contentView.layoutParams
        lp.height = measuredHeight
        contentView.layoutParams = lp
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    // 参与子孙View发起的触摸嵌套滑动和惯性嵌套滑动
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        // 若当前headerView可见，需要将headerView滑动至不可见
        val hideTop = dy > 0 && scrollY < headerView.measuredHeight
        if (hideTop) {
            val consumedDy = minOf(headerView.measuredHeight - scrollY, dy)
            LogUtils.d(dy)
            scrollBy(0, consumedDy)
            consumed[1] = consumedDy
        }
    }

    private fun fillChildScrollingView(viewGroup: ViewGroup): Boolean {
        if (viewGroup is ViewPager2) {
            val recyclerView = viewGroup.getChildAt(0)as RecyclerView
            val child = recyclerView.findViewHolderForAdapterPosition(viewGroup.currentItem)?.itemView
            if (child is ViewGroup) {
                return fillChildScrollingView(child)
            }
            return false
        }
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            when (view) {
                is RecyclerView -> {
                    scrollChildView.recyclerView = view
                    return true
                }
                is NestedScrollView -> {
                    scrollChildView.nestedScrollView = view
                    return true
                }
                is ViewGroup -> {
                    if (fillChildScrollingView(view)) return true
                }
            }
        }
        return false
    }

    class ScrollingChild {
        var recyclerView: RecyclerView? = null
        var nestedScrollView: NestedScrollView? = null
        fun clear() {
            recyclerView = null
            nestedScrollView = null
        }
    }

}