package com.donfyy.viewpager.wrap

import android.view.View
import androidx.viewpager.widget.ViewPager.PageTransformer

class PageTransform : PageTransformer {
    private val mMinAlpha = DEFAULT_MIN_ALPHA
    private val mMaxRotate = DEFAULT_MAX_ROTATE
    override fun transformPage(view: View, position: Float) {
        if (position < -1) {
            //透明度
            view.alpha = mMinAlpha
            //旋转
            view.rotation = mMaxRotate * -1
            view.pivotX = view.width.toFloat()
            view.pivotY = view.height.toFloat()
        } else if (position <= 1) {
            if (position < 0) {
                //position是0到-1的变化,p1+position就是从1到0的变化
                //(p1 - mMinAlpha) * (p1 + position)就是(p1 - mMinAlpha)到0的变化
                //再加上一个mMinAlpha，就变为1到mMinAlpha的变化。
                val factor = mMinAlpha + (1 - mMinAlpha) * (1 + position)
                view.alpha = factor
                view.rotation = mMaxRotate * position
                //position为width/2到width的变化
                view.pivotX = view.width * 0.5f * (1 - position)
                view.pivotY = view.height.toFloat()
            } else {
                //minAlpha到1的变化
                val factor = mMinAlpha + (1 - mMinAlpha) * (1 - position)
                view.alpha = factor
                view.rotation = mMaxRotate * position
                view.pivotX = view.width * 0.5f * (1 - position)
                view.pivotY = view.height.toFloat()
            }
        } else {
            view.alpha = mMinAlpha
            view.rotation = mMaxRotate
            view.pivotX = 0f
            view.pivotY = view.height.toFloat()
        }
    }

    companion object {
        private const val DEFAULT_MIN_ALPHA = 0.3f
        private const val DEFAULT_MAX_ROTATE = 15.0f
    }
}