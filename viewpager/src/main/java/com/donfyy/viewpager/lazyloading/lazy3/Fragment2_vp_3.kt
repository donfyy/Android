package com.donfyy.viewpager.lazyloading.lazy3

import com.donfyy.viewpager.R

class Fragment2_vp_3 : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_vp_3

    init {
        setFragmentDelegater(FragmentDelegater(this))
    }
}