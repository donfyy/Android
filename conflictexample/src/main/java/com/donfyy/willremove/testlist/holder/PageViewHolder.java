package com.donfyy.willremove.testlist.holder;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.donfyy.conflict.R;


/**
 * Created by Administrator on 2018/8/29.
 * Description : PageViewHolder
 */

public class PageViewHolder extends RecyclerView.ViewHolder {

    public ViewPager mViewPager;
    public RelativeLayout rlVpContainer;
    public TabLayout tabLayout;

    public PageViewHolder(View view) {
        super(view);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        rlVpContainer = view.findViewById(R.id.rl_vp_container);
        tabLayout = view.findViewById(R.id.tablayout);
    }
}