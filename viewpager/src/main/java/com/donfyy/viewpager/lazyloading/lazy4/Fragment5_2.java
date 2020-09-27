package com.donfyy.viewpager.lazyloading.lazy4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donfyy.viewpager.R;
import com.donfyy.viewpager.lazyloading.lazy3.FragmentDelegater;
import com.donfyy.viewpager.lazyloading.lazy3.LazyFragment3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Fragment5_2 extends LazyFragment3 {


    public static Fragment newIntance() {
        Fragment5_2 fragment = new Fragment5_2();
        fragment.setFragmentDelegater(new FragmentDelegater(fragment));
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_5_2;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void onFragmentFirstVisible() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
