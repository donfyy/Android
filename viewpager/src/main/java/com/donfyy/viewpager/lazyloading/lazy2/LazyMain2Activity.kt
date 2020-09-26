package com.donfyy.viewpager.lazyloading.lazy2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.donfyy.viewpager.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class LazyMain2Activity : AppCompatActivity() {
    lateinit var mViewPager: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_lazy)
        mViewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        val fragmentList: MutableList<Fragment> = ArrayList()
        fragmentList.add(FragmentExtendLazy1.newInstance(1))
        fragmentList.add(FragmentExtendLazy1.newInstance(2))
        fragmentList.add(FragmentExtendLazy1.newInstance(3))
        fragmentList.add(FragmentExtendLazy1.newInstance(4))
        fragmentList.add(FragmentExtendLazy1.newInstance(5))
        val adapter = MyFragmentPagerAdapter(supportFragmentManager, fragmentList)
        mViewPager.setAdapter(adapter)
        mViewPager.setOnPageChangeListener(viewpagerChangeListener)
    }

    var viewpagerChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
        override fun onPageSelected(i: Int) {
            var itemId = R.id.fragment_1
            when (i) {
                0 -> itemId = R.id.fragment_1
                1 -> itemId = R.id.fragment_2
                2 -> itemId = R.id.fragment_3
                3 -> itemId = R.id.fragment_4
                4 -> itemId = R.id.fragment_5
            }
            bottomNavigationView!!.selectedItemId = itemId
        }

        override fun onPageScrollStateChanged(i: Int) {}
    }
    var onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.fragment_1 -> {
                mViewPager!!.setCurrentItem(0, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_2 -> {
                mViewPager!!.setCurrentItem(1, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_3 -> {
                mViewPager!!.setCurrentItem(2, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_4 -> {
                mViewPager!!.setCurrentItem(3, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_5 -> {
                mViewPager!!.setCurrentItem(4, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}