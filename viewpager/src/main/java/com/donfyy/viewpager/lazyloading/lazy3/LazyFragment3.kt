package com.donfyy.viewpager.lazyloading.lazy3

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils

abstract class LazyFragment3 : Fragment() {
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
        // 初始化的时候，判断当前fragment可见状态
        // todo, isHidden()什么时候调用？原理
        if (!isHidden && userVisibleHint) {
            dispatchUserVisibleHint(true)
        }
        return rootView
    }

    protected abstract val layoutRes: Int
    protected abstract fun initView(view: View?)

    // 修改fragment的可见性
    // setUserVisibleHint 被调用有两种情况：
    // 1） 在切换tab的时候，会先于所有fragment的其他生命周期，先调用这个函数，可以看log
    // 2）对于之前已经调用过setUserVisibleHint 方法的fragment后，让fragment从可见到不可见之间状态的变化
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        logD("setUserVisibleHint: $isVisibleToUser")
        //  对于情况1）不予处理，用 isViewCreated 进行判断，如果isViewCreated false，说明它没有被创建
        if (isViewCreated) {
            //对于情况2）要分情况考虑，如果是不可见->可见是下面的情况 2.1），如果是可见->不可见是下面的情况2.2）
            //对于2.1）我们需要如何判断呢？首先必须是可见的（isVisibleToUser 为true）而且只有当可见状态进行改变的时候才需要切换，否则会出现反复调用的情况
            //从而导致事件分发带来的多次更新
            if (isVisibleToUser && !isSupportVisible) {
                dispatchUserVisibleHint(true)
            } else if (!isVisibleToUser && isSupportVisible) {
                dispatchUserVisibleHint(false)
            }
        }
    }

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
        if (isVisible && isParentInvisible) {
            return
        }
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
            // 在双重ViewPager嵌套的情况下，第一次滑到 Fragment 嵌套 ViewPager(fragment) 的场景的时候
            // 此时只会加载外层Fragment的数据，而不会加载内嵌ViewPager中的fragment的数据，因此，我们
            // 需要在此增加一个当外层Fragment可见的时候，分发可见事件给自己内嵌的所有Fragment显示
            dispatchChildVisibleState(true);
        } else {
            dispatchChildVisibleState(false);
            onFragmentPause()
        }
    }

    /**
     * 查询父Fragment是否可见，如果父fragment是LazyFragment3，则返回父Fragment的不可见性
     * 如果父fragment不是LazyFragment3，则返回false，都是可见的
     */
    private val isParentInvisible: Boolean
        get() {
            val parentFragment = parentFragment
            if (parentFragment is LazyFragment3) {
                return !parentFragment.isSupportVisible
            }
            return false
        }

    private fun dispatchChildVisibleState(visible: Boolean) {
        val fragmentManager = childFragmentManager
        val fragments = fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment is LazyFragment3 &&
                    !fragment.isHidden() &&
                    fragment.getUserVisibleHint()) {
                fragment.dispatchUserVisibleHint(visible)
            }
        }
    }

    /**
     *
     * 用FragmentTransaction来控制fragment的hide和show时，
     * 那么这个方法就会被调用。每当你对某个Fragment使用hide
     * 或者是show的时候，那么这个Fragment就会自动调用这个方法。
     * https://blog.csdn.net/u013278099/article/details/72869175
     * @param hidden
     */
    override fun onHiddenChanged(hidden: Boolean) {
        logD("onHiddenChanged: $hidden")
        super.onHiddenChanged(hidden)
        if (hidden) {
            dispatchUserVisibleHint(false)
        } else {
            dispatchUserVisibleHint(true)
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
        //在滑动或者跳转的过程中，第一次创建fragment的时候均会调用onResume方法，类似于在tab1 滑到tab2，此时tab3会缓存，这个时候会调用tab3 fragment的
        //onResume，所以，此时是不需要去调用 dispatchUserVisibleHint(true)的，因而出现了下面的if
        if (!mIsFirstVisible) {
            //由于Activit1 中如果有多个fragment，然后从Activity1 跳转到Activity2，此时会有多个fragment会在activity1缓存，此时，如果再从activity2跳转回
            //activity1，这个时候会将所有的缓存的fragment进行onResume生命周期的重复，这个时候我们无需对所有缓存的fragnment 调用dispatchUserVisibleHint(true)
            //我们只需要对可见的fragment进行加载，因此就有下面的if
            if (!isHidden && !isSupportVisible && userVisibleHint) {
                dispatchUserVisibleHint(true)
            }
        }
    }

    /**
     * 只有当当前页面由可见状态转变到不可见状态时才需要调用 dispatchUserVisibleHint
     * currentVisibleState && getUserVisibleHint() 能够限定是当前可见的 Fragment
     * 当前 Fragment 包含子 Fragment 的时候 dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见
     * 子 fragment 走到这里的时候自身又会调用一遍
     */
    override fun onPause() {
        logD("onPause: ")
        if (isSupportVisible) {
            dispatchUserVisibleHint(false)
        }
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