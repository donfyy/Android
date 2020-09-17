package com.donfyy.skin

import android.content.Context
import android.util.AttributeSet
import com.donfyy.skinlib.SkinManager
import com.donfyy.skinlib.SkinViewSupport
import com.google.android.material.tabs.TabLayout

class MyTabLayout
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : TabLayout(context, attrs, defStyleAttr), SkinViewSupport {
    var tabIndicatorColorResId: Int
    var tabTextColorResId: Int
    override fun applySkin() {
        if (tabIndicatorColorResId != 0) {
            val tabIndicatorColor = SkinManager.skinResources.getColor(tabIndicatorColorResId)
            setSelectedTabIndicatorColor(tabIndicatorColor)
        }
        if (tabTextColorResId != 0) {
            val tabTextColor = SkinManager.skinResources.getColorStateList(tabTextColorResId)
            tabTextColors = tabTextColor
        }
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout,
                defStyleAttr, 0)
        tabIndicatorColorResId = a.getResourceId(R.styleable.TabLayout_tabIndicatorColor, 0)
        tabTextColorResId = a.getResourceId(R.styleable.TabLayout_tabTextColor, 0)
        a.recycle()
    }
}