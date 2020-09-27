package com.donfyy.viewpager.lazyloading.lazy6

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.donfyy.viewpager.R
import java.util.*

class Fragment2WithViewPager : BaseFragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var fragmentsList: ArrayList<Fragment>
    override val isLoading = false

    override val layoutRes: Int
        get() = R.layout.fragment_2_vp

    override fun initView(view: View?) {
        super.initView(view)
        view ?: return
        viewPager = view.findViewById(R.id.content)
        fragmentsList = ArrayList()
        fragmentsList.add(Fragment2_vp_1())
        fragmentsList.add(Fragment2_vp_2())
        fragmentsList.add(Fragment2_vp_3())
        fragmentsList.add(Fragment2_vp_4())
        val pagerAdapter: PagerAdapter = object : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(i: Int): Fragment {
                return fragmentsList[i]
            }

            override fun getCount(): Int {
                return fragmentsList.size
            }
        }
        viewPager.setAdapter(pagerAdapter)
    }

    init {
        setFragmentDelegater(FragmentDelegater(this))
    }
}