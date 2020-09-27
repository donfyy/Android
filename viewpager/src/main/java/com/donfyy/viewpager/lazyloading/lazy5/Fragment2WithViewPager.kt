package com.donfyy.viewpager.lazyloading.lazy5

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.donfyy.viewpager.R
import java.util.*

class Fragment2WithViewPager : BaseFragment() {
    private lateinit var viewPager //对应的viewPager
            : ViewPager
    private lateinit var fragmentsList //view数组
            : ArrayList<Fragment>

    override val layoutRes: Int
        get() = R.layout.fragment_2_vp

    override fun initView(view: View?) {
        super.initView(view)
        view?:return
        viewPager = view.findViewById(R.id.content)
        fragmentsList = ArrayList()
        fragmentsList.add(Fragment2_vp_1())
        fragmentsList.add(Fragment2_vp_2())
        fragmentsList.add(Fragment2_vp_3())
        fragmentsList.add(Fragment2_vp_4())
        val pagerAdapter = object : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(i: Int): Fragment {
                return fragmentsList[i]
            }

            override fun getCount(): Int {
                return fragmentsList.size
            }
        }
        viewPager.adapter = pagerAdapter
    }

    override fun onFragmentFirstVisible() {
    }

    init {
        setFragmentDelegater(FragmentDelegater(this))
    }
}