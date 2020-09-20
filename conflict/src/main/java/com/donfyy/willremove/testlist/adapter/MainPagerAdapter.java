package com.donfyy.willremove.testlist.adapter;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.donfyy.willremove.testlist.fragment.PagerFragment;

import java.util.List;

/**
 * Created by song on 2018/8/22 0022
 * My email : logisong@163.com
 * The role of this :
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<String> data;
    private List<PagerFragment> fragments;
    public MainPagerAdapter(FragmentManager fm, List<String> data, List<PagerFragment> fragments) {
        super(fm);
        this.data = data;
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position);
    }
}
