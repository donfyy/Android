package com.donfyy.viewpager.lazyloading.lazy3

import android.view.View
import com.donfyy.viewpager.R

class Fragment4 : BaseFragment() {
    override fun initView(view: View?) {
        super.initView(view)
        content.setBackgroundColor(resources.getColor(R.color.fragment_4_bg))
    }

    init {
        setFragmentDelegater(FragmentDelegater(this))
    }
}