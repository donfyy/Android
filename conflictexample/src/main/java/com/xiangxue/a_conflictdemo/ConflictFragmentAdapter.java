package com.xiangxue.a_conflictdemo;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class ConflictFragmentAdapter extends FragmentPagerAdapter {

    public ConflictFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        Bundle bundle = new Bundle();
        bundle.putString("index", pos+"");
        Fragment fragment = new ConflictFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }
    
    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
    }
}