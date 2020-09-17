package com.donfyy.skinlib

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.donfyy.skinlib.SkinViewManager.Companion.BACKGROUND
import com.donfyy.skinlib.SkinViewManager.Companion.SRC
import com.donfyy.skinlib.SkinViewManager.Companion.TEXT_COLOR

class SkinView(val view: View, val attrs: Map<String, Int>) {

    // 读取皮肤包中的属性值更新view
    fun applySkin() {
        if (view is SkinViewSupport) view.applySkin()
        for ((attrName, resId) in attrs) {
            when (attrName) {
                BACKGROUND -> {
                    when (val obj = SkinManager.skinResources.getBackground(resId)) {
                        is Int -> view.setBackgroundColor(obj)
                        is Drawable -> ViewCompat.setBackground(view, obj)
                    }
                }
                SRC -> {
                    if (view is ImageView) {
                        when (val obj = SkinManager.skinResources.getBackground(resId)) {
                            is Int -> view.setImageDrawable(ColorDrawable(obj))
                            is Drawable -> view.setImageDrawable(obj)
                        }
                    }
                }
                TEXT_COLOR -> {
                    if (view is TextView) {
                        view.setTextColor(SkinManager.skinResources.getColorStateList(resId))
                    }
                }
            }
        }
    }
}