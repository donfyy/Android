package com.donfyy.skinlib.utils

import android.content.Context


object ThemeUtils {
    // 返回theme中属性定义的资源id
    fun getResId(context: Context, attrs: IntArray): IntArray {
        val resIds = IntArray(attrs.size)
        context.obtainStyledAttributes(attrs).apply {
            attrs.forEachIndexed { idx, attrId -> resIds[idx] = getResourceId(attrId, 0) }
            recycle()
        }
        return resIds
    }
}