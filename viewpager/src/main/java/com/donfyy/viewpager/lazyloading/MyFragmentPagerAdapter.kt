package com.donfyy.viewpager.lazyloading

import android.util.Log

class MyFragmentPagerAdapter(fm: androidx.fragment.app.FragmentManager?, private val fragmentList: List<androidx.fragment.app.Fragment>) : androidx.fragment.app.FragmentPagerAdapter(fm!!) {
    override fun getItem(i: Int): androidx.fragment.app.Fragment {
        Log.d(TAG, "getItem: $i")
        return fragmentList[i]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    companion object {
        private const val TAG = "MyFragmentPagerAdapter"
    }

}