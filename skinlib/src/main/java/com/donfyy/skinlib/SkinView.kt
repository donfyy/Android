package com.donfyy.skinlib

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat
import com.donfyy.skinlib.SkinViewManager.Companion.BACKGROUND

class SkinView(val view: View, val attrs: Map<String, Int>) {

    // 读取皮肤包中的属性值更新view
    fun applySkin() {
        if (view is SkinViewSupport) view.applySkin()
        for ((attrName, resId) in attrs) {
            when (attrName) {
                BACKGROUND -> {
                    when (val obj = SkinManager.resourceManager.getBackground(resId)) {
                        is Int -> view.setBackgroundColor(obj)
                        is Drawable -> ViewCompat.setBackground(view, obj)
                    }
                }
            }
        }
    }
}