package com.donfyy.viewpager.lazyloading.lazy3

import android.view.View
import com.donfyy.viewpager.R

abstract class BaseFragment : LazyFragment3() {
    lateinit var loading: View
    lateinit var content: View
    override fun initView(view: View?) {
        view?.apply {
            loading = findViewById(R.id.tv_loading)
            content = findViewById(R.id.content)
        }
    }

    override val layoutRes: Int
        get() = R.layout.fragment_1

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        loading.rootView.postDelayed({
            content.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }, 1000)
    }
}