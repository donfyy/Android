package com.donfyy.e_conflictfixcases.nestedscroll

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.donfyy.conflict.R
import com.donfyy.conflict.databinding.ActivityNestedViewPagerBinding
import com.donfyy.e_conflictfixcases.nestedscroll.fragment.RecyclerViewFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy

class NestedViewPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNestedViewPagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nested_view_pager)
        val data = mutableListOf<Fragment>(RecyclerViewFragment(), RecyclerViewFragment(), RecyclerViewFragment())
        val pagerAdapter = ViewPagerAdapter(this, data)
        binding.viewpagerView.adapter = pagerAdapter
        // binding.comboContentView.setOffscreenPageLimit(1);
        val labels = arrayOf("linear", "scroll", "recycler")
        val tabConfigurationStrategy = TabConfigurationStrategy { tab, position -> tab.text = labels[position] }
        TabLayoutMediator(binding.tablayout, binding.viewpagerView, tabConfigurationStrategy).attach()
        binding.swipeRefreshLayout.setOnRefreshListener {
            Log.e("NestedViewPagerActivity", "Refresh started...")
            binding.root.postDelayed({ binding.swipeRefreshLayout.isRefreshing = false }, 1000)
        }
    }

}