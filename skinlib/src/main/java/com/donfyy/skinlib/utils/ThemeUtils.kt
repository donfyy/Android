package com.donfyy.skinlib.utils

import android.content.Context
import com.blankj.utilcode.util.LogUtils


object ThemeUtils {
    // 返回theme中属性定义的资源id
    fun getResId(context: Context, attrs: IntArray): IntArray {
        val resIds = IntArray(attrs.size)
        context.obtainStyledAttributes(attrs).apply {
            attrs.forEachIndexed {
                idx, attrId ->
                LogUtils.d("attrValue", attrId, getResourceId(idx, 0))
                resIds[idx] = getResourceId(idx, 0) }
            recycle()
        }
        return resIds
    }
}