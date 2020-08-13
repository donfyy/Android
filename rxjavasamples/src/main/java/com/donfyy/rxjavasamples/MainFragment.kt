package com.donfyy.rxjavasamples

import android.os.Bundle
import android.view.View
import com.donfyy.base.BaseFragment
import com.donfyy.rxjavasamples.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.Flowable

class MainFragment : BaseFragment<FragmentMainBinding>() {
    override var layoutId: Int = R.layout.fragment_main
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.flowableJust.setOnClickListener { view ->
            Flowable.just("hi").subscribe {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}