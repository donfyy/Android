package com.donfyy.skinlib

import android.util.AttributeSet
import android.view.View
import com.donfyy.skinlib.utils.ThemeUtils

class SkinViewManager {
    companion object {
        const val BACKGROUND = "background"
        const val SRC = "src"
        const val TEXT_COLOR = "textColor"
        // 预定义需要换肤的属性
        val skinAttrs = setOf(BACKGROUND, SRC, TEXT_COLOR)
    }

    private val skinViews = mutableListOf<SkinView>()

    // 查找view在xml中用到的属性值并保存到集合中
    fun look(view: View, attrs: AttributeSet) {
        val map = HashMap<String, Int>()
        for (i in 0 until attrs.attributeCount) {
            val attrName = attrs.getAttributeName(i)
            if (attrName !in skinAttrs) continue
            val attrValue = attrs.getAttributeValue(i)
            val resId = when {
                attrValue.startsWith("@") -> attrValue.substring(1).toInt()
                attrValue.startsWith("?") -> {
                    // 引用的是？后属性引用的资源id
                    ThemeUtils.getResId(view.context, intArrayOf(attrValue.substring(1).toInt()))[0]
                }
                // 硬编码的属性值无法改变
                else -> 0
            }
            if (resId != 0) {
                map[attrName] = resId
            }
        }
        if (map.isNotEmpty() || view is SkinViewSupport) {
            skinViews += SkinView(view, map).apply { applySkin() }
        }
    }

    fun applySkin() {
        skinViews.forEach { it.applySkin() }
    }
}