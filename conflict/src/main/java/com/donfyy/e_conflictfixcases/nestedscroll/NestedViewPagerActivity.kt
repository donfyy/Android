package com.donfyy.e_conflictfixcases.nestedscroll

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.donfyy.conflict.R
import com.donfyy.conflict.databinding.ActivityNestedViewPagerBinding

class NestedViewPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNestedViewPagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nested_view_pager)
//        val data = mutableListOf(NestedScrollViewFragment(),
//                RecyclerViewFragment(), RecyclerViewFragment(), RecyclerViewFragment())
//        val pagerAdapter = ViewPagerAdapter(this, data)
//        binding.viewpagerView.adapter = pagerAdapter
        // binding.comboContentView.setOffscreenPageLimit(1)
//        val labels = arrayOf("简况", "linear", "scroll", "recycler")
//        val tabConfigurationStrategy = TabConfigurationStrategy { tab, position -> tab.text = labels[position] }
//        TabLayoutMediator(binding.tablayout, binding.viewpagerView, tabConfigurationStrategy).attach()
        binding.swipeRefreshLayout.setOnRefreshListener {
            Log.e("NestedViewPagerActivity", "Refresh started...")
            binding.root.postDelayed({ binding.swipeRefreshLayout.isRefreshing = false }, 1000)
        }
    }

}