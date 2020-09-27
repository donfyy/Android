package com.donfyy.viewpager.lazyloading.lazy6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.donfyy.viewpager.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class LazyMain6Activity : AppCompatActivity() {
    private lateinit var viewPager: androidx.viewpager.widget.ViewPager
    private lateinit var fragmentsList: ArrayList<androidx.fragment.app.Fragment>
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vp10)
        viewPager = findViewById(R.id.viewpager01)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        fragmentsList = ArrayList()
        fragmentsList.add(Fragment1())
        fragmentsList.add(Fragment2WithViewPager())
        fragmentsList.add(Fragment3())
        fragmentsList.add(Fragment4())
        fragmentsList.add(Fragment5())
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
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
        })
        val pagerAdapter: androidx.viewpager.widget.PagerAdapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(i: Int): androidx.fragment.app.Fragment {
                return fragmentsList[i]
            }

            override fun getCount(): Int {
                return fragmentsList.size
            }
        }
        viewPager.adapter = pagerAdapter
    }

    private var onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.fragment_1 -> {
                viewPager.setCurrentItem(0, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_2 -> {
                viewPager.setCurrentItem(1, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_3 -> {
                viewPager.setCurrentItem(2, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_4 -> {
                viewPager.setCurrentItem(3, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.fragment_5 -> {
                viewPager.setCurrentItem(4, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}