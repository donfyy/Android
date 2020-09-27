package com.donfyy.viewpager.lazyloading.lazy6

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils

abstract class LazyFragment6 : Fragment() {
    // fragment 生命周期：
    // onAttach -> onCreate -> onCreatedView -> onActivityCreated -> onStart -> onResume -> onPause -> onStop -> onDestroyView -> onDestroy -> onDetach
    // 对于 ViewPager + Fragment 的实现我们需要关注的几个生命周期有：
    // onCreatedView + onActivityCreated + onResume + onPause + onDestroyView
    protected var rootView: View? = null
    var isViewCreated = false
    private var isSupportVisible = false
    var mIsFirstVisible = true
    var mFragmentDelegater: FragmentDelegater? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (rootView == null) {
            rootView = inflater.inflate(layoutRes, container, false)
        }
        initView(rootView)
        isViewCreated = true
        logD("onCreateView: ")
        return rootView
    }

    protected abstract val layoutRes: Int
    protected abstract fun initView(view: View?)

    /**
     * 对于一层fragment，要考虑如下3个因素
     * 1.生命周期方法created/resumed
     * 2.setUserVisibleHint
     * 3.onHiddenChanged
     * 对于内层的fragment，要多考虑一个因素
     * 4.父Fragment的可见性
     * 统一处理用户可见信息分发
     */
    private fun dispatchUserVisibleHint(isVisible: Boolean) {
        logD("dispatchUserVisibleHint: $isVisible")
        //为了代码严谨
        if (isSupportVisible == isVisible) {
            return
        }
        isSupportVisible = isVisible
        if (isVisible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false
                onFragmentFirstVisible()
            }
            onFragmentResume()
        } else {
            onFragmentPause()
        }
    }

    protected open fun onFragmentFirstVisible() {
        logD("onFragmentFirstVisible")
    }

    protected fun onFragmentResume() {
        logD("onFragmentResume " + " 真正的resume,开始相关操作耗时")
    }

    protected fun onFragmentPause() {
        logD("onFragmentPause" + " 真正的Pause,结束相关操作耗时")
    }

    fun setFragmentDelegater(fragmentDelegater: FragmentDelegater?) {
        mFragmentDelegater = fragmentDelegater
    }

    override fun onResume() {
        super.onResume()
        logD("onResume: ")
        dispatchUserVisibleHint(true)
    }

    /**
     * 只有当当前页面由可见状态转变到不可见状态时才需要调用 dispatchUserVisibleHint
     * currentVisibleState && getUserVisibleHint() 能够限定是当前可见的 Fragment
     * 当前 Fragment 包含子 Fragment 的时候 dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见
     * 子 fragment 走到这里的时候自身又会调用一遍
     */
    override fun onPause() {
        logD("onPause: ")
        dispatchUserVisibleHint(false)
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        logD("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logD("onDestroyView")
        isViewCreated = false
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    fun logD(infor: String) {
        mFragmentDelegater?.dumpLifeCycle(infor) ?: LogUtils.d(infor)
    }
}