package com.donfyy.wrap

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class MyViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            // 先去测量下每一个子View
            child.measure(widthMeasureSpec, getChildMeasureSpec(MeasureSpec.getSize(heightMeasureSpec),
                    paddingLeft + paddingRight, child.layoutParams.height))
            val h = child.measuredHeight
            // 记下最大高度
            if (h > height) {
                height = h
            }
        }
        if (height == 0) {
            height = MeasureSpec.getSize(heightMeasureSpec)
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)))
    }
}