package com.donfyy.viewpager.lazyloading.lazy6

import com.donfyy.viewpager.R

class Fragment4 : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_4

    init {
        setFragmentDelegater(FragmentDelegater(this))
    }
}