package com.donfyy.a_now;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.donfyy.conflict.R;
import com.donfyy.conflict.databinding.FragmentHomeBinding;

public class HeadlineNewsActivity extends AppCompatActivity {
    public HeadlineNewsFragmentAdapter adapter;
    FragmentHomeBinding viewDataBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.fragment_home);
        adapter = new HeadlineNewsFragmentAdapter(getSupportFragmentManager());
        viewDataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewDataBinding.viewpager.setAdapter(adapter);
        viewDataBinding.tablayout.setupWithViewPager(viewDataBinding.viewpager);
        viewDataBinding.viewpager.setOffscreenPageLimit(1);
    }
}
