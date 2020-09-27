package com.donfyy.viewpager.lazyloading.lazy6

import com.donfyy.viewpager.R

class Fragment1 : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_1

    init {
        setFragmentDelegater(FragmentDelegater(this))
    }
}