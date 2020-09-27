package com.donfyy.viewpager.lazyloading.lazy5

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils

abstract class LazyFragment5 : Fragment() {
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
        // 事实上作为父 Fragment 的 BottomTabFragment2 并没有分发可见事件，
        // 他通过 getUserVisibleHint() 得到的结果为 false，首先我想到的
        // 是能在负责分发事件的方法中判断一下当前父 fragment 是否可见，
        // 如果父 fragment 不可见我们就不进行可见事件的分发

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

    protected abstract fun onFragmentFirstVisible()
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
        mIsFirstVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun logD(infor: String) {
        mFragmentDelegater?.dumpLifeCycle(infor) ?: LogUtils.d(infor)
    }
}