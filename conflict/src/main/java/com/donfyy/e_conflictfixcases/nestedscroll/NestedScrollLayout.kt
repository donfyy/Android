package com.donfyy.e_conflictfixcases.nestedscroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.donfyy.e_conflictfixcases.nestedscroll.utils.FlingHelper

class NestedScrollLayout
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0)
    : NestedScrollView(context, attrs, defStyleAttr) {
    private lateinit var headerView: View
    private lateinit var contentView: ViewGroup
    private var flingHelper: FlingHelper = FlingHelper(this.context)
    private var totalDy = 0

    // 用于判断RecyclerView是否在fling
    private var isStartFling = false

    // 记录当前滑动的y轴加速度
    private var velocityY = 0

    init {
        setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (isStartFling) {
                totalDy = 0
                isStartFling = false
            }
            if (scrollY == 0) {
                Log.i("xxxxxx", "TOP SCROLL")
                // refreshLayout.setEnabled(true);
            }
            if (scrollY == getChildAt(0).measuredHeight - v.measuredHeight) {
                Log.i("xxxxxx", "BOTTOM SCROLL")
                dispatchChildFling()
            }
            //在RecyclerView fling情况下，记录当前RecyclerView在y轴的偏移
            totalDy += scrollY - oldScrollY
        }
    }

    private fun dispatchChildFling() {
        if (velocityY != 0) {
            val splineFlingDistance = flingHelper!!.getSplineFlingDistance(velocityY)
            if (splineFlingDistance > totalDy) {
                childFling(flingHelper!!.getVelocityByDistance(splineFlingDistance - java.lang.Double.valueOf(totalDy.toDouble())))
            }
        }
        totalDy = 0
        velocityY = 0
    }

    private fun childFling(velY: Int) {
        val childRecyclerView = getChildRecyclerView(contentView)
        childRecyclerView?.fling(0, velY)
    }

    override fun fling(velocityY: Int) {
        super.fling(velocityY)
        if (velocityY <= 0) {
            this.velocityY = 0
        } else {
            isStartFling = true
            this.velocityY = velocityY
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        headerView = (getChildAt(0) as ViewGroup).getChildAt(0)
        contentView = (getChildAt(0) as ViewGroup).getChildAt(1) as ViewGroup
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 调整contentView的高度为父容器高度，使之填充布局，避免父容器滚动后出现空白
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val lp = contentView!!.layoutParams
        lp.height = measuredHeight
        contentView!!.layoutParams = lp
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.i("NestedScrollLayout", scrollY.toString() + "::onNestedPreScroll::" + headerView!!.measuredHeight)
        // 向上滑动。若当前topview可见，需要将topview滑动至不可见
        val hideTop = dy > 0 && scrollY < headerView!!.measuredHeight
        if (hideTop) {
            scrollBy(0, dy)
            consumed[1] = dy
        }
    }

    private fun getChildRecyclerView(viewGroup: ViewGroup?): RecyclerView? {
        for (i in 0 until viewGroup!!.childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is RecyclerView && view.javaClass == RecyclerView::class.java) {
                return viewGroup.getChildAt(i) as RecyclerView
            } else if (viewGroup.getChildAt(i) is ViewGroup) {
                val childRecyclerView: ViewGroup? = getChildRecyclerView(viewGroup.getChildAt(i) as ViewGroup)
                if (childRecyclerView is RecyclerView) {
                    return childRecyclerView
                }
            }
            continue
        }
        return null
    }

}