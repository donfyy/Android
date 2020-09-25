package com.donfyy.viewpager.wrap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.donfyy.viewpager.R

class MyPagerAdapter internal constructor(private val mImages: List<Int>, private val mContext: Context) : PagerAdapter() {
    override fun getPageTitle(position: Int): CharSequence? {
        return position.toString()
    }

    override fun getCount(): Int {
        return Int.MAX_VALUE
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pos = position % mImages.size
        val view = LayoutInflater.from(mContext).inflate(R.layout.linear_item, container, false)
        val textView = view.findViewById<TextView>(R.id.tv)
        textView.text = pos.toString()
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getPageWidth(position: Int): Float {
        return 1f
    }
}