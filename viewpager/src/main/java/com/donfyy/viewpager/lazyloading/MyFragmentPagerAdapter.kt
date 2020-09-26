package com.donfyy.viewpager.lazyloading

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class MyFragmentPagerAdapter(fm: FragmentManager, private val fragmentList: List<Fragment>)
    : FragmentStatePagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {
    override fun getItem(i: Int): Fragment {
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