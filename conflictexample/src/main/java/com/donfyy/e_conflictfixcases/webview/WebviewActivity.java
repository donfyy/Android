package com.donfyy.e_conflictfixcases.webview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.donfyy.conflict.R;
import com.donfyy.conflict.databinding.FragmentHomeBinding;

public class WebviewActivity extends AppCompatActivity {
    public WebviewFragmentAdapter adapter;
    FragmentHomeBinding viewDataBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.fragment_home);
        adapter = new WebviewFragmentAdapter(getSupportFragmentManager());
        viewDataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewDataBinding.viewpager.setAdapter(adapter);
        viewDataBinding.tablayout.setupWithViewPager(viewDataBinding.viewpager);
        viewDataBinding.viewpager.setOffscreenPageLimit(1);
    }
}
