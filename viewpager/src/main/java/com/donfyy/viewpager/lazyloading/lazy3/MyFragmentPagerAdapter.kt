/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.donfyy.viewpager.lazyloading.lazy3

import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.ViewGroup

/**
 * FragmentPagerAdapter派生自PagerAdapter，它是用来呈现Fragment页面的，这些Fragment页面会一直保存在fragment manager中，以便用户可以随时取用。
 * 这个适配器最好用于有限个静态fragment页面的管理。尽管不可见的视图有时会被销毁，
 * 但用户所有访问过的fragment都会被保存在内存中。因此fragment实例会保存大量的各种状态，这就造成了很大的内存开销。所以如果要处理大量的页面切换，建议使用FragmentStatePagerAdapter.
 * 每一个使用FragmentPagerAdapter的ViewPager都要有一个有效的ID集合[.makeFragmentName]，有效ID的集合就是Fragment的集合
 * 对于FragmentPagerAdapter的派生类，只需要重写getItem(int)和getCount()就可以了
 */
abstract class MyFragmentPagerAdapter(private val mFragmentManager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(mFragmentManager) {
    private var mCurTransaction: androidx.fragment.app.FragmentTransaction? = null
    private var mCurrentPrimaryItem: androidx.fragment.app.Fragment? = null

    /**
     * Return the Fragment associated with a specified position.
     */
    abstract override fun getItem(position: Int): androidx.fragment.app.Fragment
    override fun startUpdate(container: ViewGroup) {
        check(container.id != View.NO_ID) {
            ("ViewPager with adapter " + this
                    + " requires a view id")
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        Log.i("Zero","position: " + position);
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction()
        }
        val itemId = getItemId(position)

        // Do we already have this fragment?
        val name = makeFragmentName(container.id, itemId)
        var fragment = mFragmentManager.findFragmentByTag(name)
        if (fragment != null) {
            if (DEBUG) Log.v(TAG, "Attaching item #$itemId: f=$fragment")
            mCurTransaction!!.attach(fragment)
        } else {
            fragment = getItem(position)
            if (DEBUG) Log.v(TAG, "Adding item #$itemId: f=$fragment")
            mCurTransaction!!.add(container.id, fragment,
                    makeFragmentName(container.id, itemId))
        }
        if (fragment !== mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false)
            fragment.userVisibleHint = false
        }
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction()
        }
        if (DEBUG) Log.v(TAG, "Detaching item #" + getItemId(position) + ": f=" + `object`
                + " v=" + (`object` as androidx.fragment.app.Fragment).view)
        mCurTransaction!!.detach((`object` as androidx.fragment.app.Fragment))
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        val fragment = `object` as androidx.fragment.app.Fragment
        if (fragment !== mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem!!.setMenuVisibility(false)
                mCurrentPrimaryItem!!.userVisibleHint = false
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true)
                fragment.userVisibleHint = true
            }
            mCurrentPrimaryItem = fragment
        }
    }

    override fun finishUpdate(container: ViewGroup) {
        if (mCurTransaction != null) {
            mCurTransaction!!.commitNowAllowingStateLoss()
            mCurTransaction = null
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (`object` as androidx.fragment.app.Fragment).view === view
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    companion object {
        private const val TAG = "MyFragmentPagerAdapter"
        private const val DEBUG = false
        private fun makeFragmentName(viewId: Int, id: Long): String {
//        Log.i("Zero", "makeFragmentName viewId: " + viewId + ", id: " + id);
            return "android:switcher:$viewId:$id"
        }
    }

}