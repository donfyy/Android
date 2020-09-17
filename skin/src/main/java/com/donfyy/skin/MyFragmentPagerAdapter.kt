package com.donfyy.skin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyFragmentPagerAdapter(fragmentManager: FragmentManager?, private val mFragments: List<Fragment>,
                             private val mTitles: List<String>) : FragmentPagerAdapter(fragmentManager!!) {
    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }

}