package com.xiangxue.a_now;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.xiangxue.conflict.R;
import com.xiangxue.conflict.databinding.FragmentBinding;

public class NewsListFragment  extends Fragment {
    FragmentBinding viewDataBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment, container, false);
        String text = "";
        for(int i = 0; i < 100; i++){
            text = text + getArguments().get("index");
        }
        viewDataBinding.textview.setText(text);
        return viewDataBinding.getRoot();
    }

}