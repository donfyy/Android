package com.donfyy.rxjavasamples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.donfyy.rxjavasamples.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.Flowable

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = DataBindingUtil.inflate<FragmentMainBinding>(inflater, R.layout.fragment_main, container, false)
        inflate.flowableJust.setOnClickListener { view ->
            Flowable.just("Hi~").subscribe { Snackbar.make(view, it, Snackbar.LENGTH_LONG).show() }
        }
        return inflate.root
    }
}