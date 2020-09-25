package com.donfyy.viewpager.lazyloading

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.donfyy.viewpager.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class LazyMainActivity : AppCompatActivity() {
    private lateinit var mViewPager: androidx.viewpager.widget.ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_lazy)
        mViewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        val fragmentList = ArrayList<androidx.fragment.app.Fragment>()
        fragmentList.add(MyFragment.newInstance(1))
        fragmentList.add(MyFragment.newInstance(2))
        fragmentList.add(MyFragment.newInstance(3))
        fragmentList.add(MyFragment.newInstance(4))
        fragmentList.add(MyFragment.newInstance(5))
        val adapter = MyFragmentPagerAdapter(supportFragmentManager, fragmentList)
        mViewPager.adapter = adapter
        mViewPager.offscreenPageLimit = 2
        mViewPager.setOnPageChangeListener(viewpagerChangeListener)
    }

    private var viewpagerChangeListener: OnPageChangeListener = object : OnPageChangeListener {
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
            bottomNavigationView.selectedItemId = itemId
        }

        override fun onPageScrollStateChanged(i: Int) {}
    }
    private var onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.fragment_1 -> {
                mViewPager.setCurrentItem(0, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_2 -> {
                mViewPager.setCurrentItem(1, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_3 -> {
                mViewPager.setCurrentItem(2, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_4 -> {
                mViewPager.setCurrentItem(3, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_5 -> {
                mViewPager.setCurrentItem(4, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}