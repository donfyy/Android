package com.donfyy.viewexample.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.max

class ViewGroupDemo1 @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    var offset = 50
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var i = 0
        while (i < childCount) {

            val child = getChildAt(i)

            val lp = child.layoutParams
            child.measure(
                    getChildMeasureSpec(widthMeasureSpec, 0, lp.width),
                    getChildMeasureSpec(heightMeasureSpec, 0, lp.height)
            )

            i++
        }

        var maxWidthSize = 0
        i = 0
        while (i < childCount) {
            val child = getChildAt(i)

            val childWidth = child.measuredWidth + i * offset
            maxWidthSize = max(childWidth, maxWidthSize)

            i++
        }
        var maxHeightSize = 0
        i = 0
        while (i < childCount) {
            val child = getChildAt(i)
            maxHeightSize += child.height

            i++
        }

        setMeasuredDimension(
                View.resolveSize(maxWidthSize, widthMeasureSpec),
                View.resolveSize(maxHeightSize, widthMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        var l = 0
        var t = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            l += offset
            child.layout(
                    l,
                    t,
                    l + child.measuredWidth,
                    t + child.measuredHeight
            )
            t += child.measuredHeight
        }

    }
}