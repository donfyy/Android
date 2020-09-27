package com.donfyy.viewpager.lazyloading.lazy4

import com.donfyy.viewpager.R

class Fragment2_vp_1 : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_vp_1

    init {
        setFragmentDelegater(FragmentDelegater(this))
    }
}