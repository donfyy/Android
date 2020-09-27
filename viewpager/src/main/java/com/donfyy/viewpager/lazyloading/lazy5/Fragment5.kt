package com.donfyy.viewpager.lazyloading.lazy5

import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.donfyy.viewpager.R

open class Fragment5 : BaseFragment() {
    private lateinit var mReplace: Button
    private lateinit var mHideShow: Button
    private lateinit var mCurrentFragment: Fragment
    private lateinit var fragment51: Fragment
    private lateinit var fragment52: Fragment
    override val layoutRes: Int
        get() = R.layout.fragment_5

    override fun initView(view: View?) {
        super.initView(view)
        view ?: return
        mReplace = view.findViewById(R.id.btnReplace)
        mHideShow = view.findViewById(R.id.btnHideShow)
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragment51 = Fragment5_1.newIntance()
        mCurrentFragment = fragment51
        fragmentTransaction.add(R.id.frameLayout_5, mCurrentFragment!!, Fragment5_1::class.java.name)
        fragmentTransaction.show(mCurrentFragment!!)
        fragmentTransaction.commit()
        mReplace.setOnClickListener(View.OnClickListener {
            val fragmentTransaction = childFragmentManager.beginTransaction()
            if (mCurrentFragment is Fragment5_1) {
                fragment52 = Fragment5_2.newIntance()
                mCurrentFragment = fragment52
                fragmentTransaction.replace(R.id.frameLayout_5, mCurrentFragment!!, Fragment5_2::class.java.name)
                fragmentTransaction.show(mCurrentFragment!!).commitNowAllowingStateLoss()
            } else {
                fragment51 = Fragment5_1.newIntance()
                mCurrentFragment = fragment51
                fragmentTransaction.replace(R.id.frameLayout_5, mCurrentFragment!!, Fragment5_1::class.java.name)
                fragmentTransaction.show(mCurrentFragment!!).commitNowAllowingStateLoss()
            }
        })
        mHideShow.setOnClickListener(View.OnClickListener {
            val fragmentTransaction = childFragmentManager.beginTransaction()
            if (mCurrentFragment is Fragment5_1) {
                val tmp = childFragmentManager.findFragmentByTag(Fragment5_2::class.java.name)
                fragmentTransaction.hide(mCurrentFragment)
                if (tmp == null) {
                    fragment52 = Fragment5_2.newIntance()
                    mCurrentFragment = fragment52
                    fragmentTransaction.add(R.id.frameLayout_5, mCurrentFragment!!, Fragment5_2::class.java.name)
                } else {
                    mCurrentFragment = tmp
                }
                fragmentTransaction.show(mCurrentFragment!!).commitNowAllowingStateLoss()
            } else {
                val tmp = childFragmentManager.findFragmentByTag(Fragment5_1::class.java.name)
                fragmentTransaction.hide(mCurrentFragment!!)
                if (tmp == null) {
                    fragment51 = Fragment5_1.newIntance()
                    mCurrentFragment = fragment51
                    fragmentTransaction.add(R.id.frameLayout_5, mCurrentFragment!!, Fragment5_1::class.java.name)
                } else {
                    mCurrentFragment = tmp
                }
                fragmentTransaction.show(mCurrentFragment!!).commitNowAllowingStateLoss()
            }
        })
    }

    init {
        setFragmentDelegater(FragmentDelegater(this))
    }
}