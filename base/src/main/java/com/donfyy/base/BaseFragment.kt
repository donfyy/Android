package com.donfyy.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<B> : Fragment() where B : ViewDataBinding {
    protected abstract var layoutId: Int
    protected lateinit var viewDataBinding: B
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<B>(inflater, layoutId, container, false).let {
            viewDataBinding = it
            it.root
        }
    }
}