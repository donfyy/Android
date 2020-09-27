package com.donfyy.viewpager.lazyloading.lazy6

import android.view.View
import com.donfyy.viewpager.R

abstract class BaseFragment : LazyFragment6() {
    lateinit var loading: View
    lateinit var content: View
    open val isLoading = true
    override fun initView(view: View?) {
        view?.apply {
            loading = findViewById(R.id.tv_loading)
            content = findViewById(R.id.content)
            view.setOnClickListener {
                change()
            }
        }
    }

    private fun change() {
        if (content.visibility == View.VISIBLE) {
            content.visibility = View.INVISIBLE
            loading.visibility = View.VISIBLE
        } else {
            content.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }
    }

    override val layoutRes: Int
        get() = R.layout.fragment_1

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        if (!isLoading) return
        loading.rootView.postDelayed({
            logD("onFragmentFirstVisible after delayed")
            change()
       }, 500)
    }
}