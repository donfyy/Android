package com.xiangxue.e_conflictfixcases.x5webview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.xiangxue.conflict.R;
import com.xiangxue.conflict.databinding.FragmentHomeBinding;

public class X5WebviewActivity extends AppCompatActivity {
    public X5WebviewFragmentAdapter adapter;
    FragmentHomeBinding viewDataBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.fragment_home);
        adapter = new X5WebviewFragmentAdapter(getSupportFragmentManager());
        viewDataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewDataBinding.viewpager.setAdapter(adapter);
        viewDataBinding.tablayout.setupWithViewPager(viewDataBinding.viewpager);
        viewDataBinding.viewpager.setOffscreenPageLimit(1);
    }
}
