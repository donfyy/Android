package com.donfyy.skinlib.utils

import android.app.Activity
import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.donfyy.skinlib.SkinManager


object ThemeUtils {
    private val STATUS_BAR_ATTR_IDS = intArrayOf(android.R.attr.statusBarColor, android.R.attr.navigationBarColor)
    private val COLOR_PRIMARY_DARK = intArrayOf(androidx.appcompat.R.attr.colorPrimaryDark)
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

    fun updateStatusBar(activity: Activity) {
        val (statusBarColorResId, navigationBarColorResId) = getResId(activity, STATUS_BAR_ATTR_IDS)
        if (statusBarColorResId != 0) {
            LogUtils.d("set status bar color with res id")
            activity.window.statusBarColor = SkinManager.skinResources.getColor(statusBarColorResId)
        } else {
            val (resId) = getResId(activity, COLOR_PRIMARY_DARK)
            if (resId != 0) {
                LogUtils.d("set status bar color with color primary dark")
                activity.window.statusBarColor = SkinManager.skinResources.getColor(resId)
            }
        }
        if (navigationBarColorResId != 0) {
            LogUtils.d("set navigation bar color with res id")
            activity.window.navigationBarColor = SkinManager.skinResources.getColor(navigationBarColorResId)
        }
    }
}