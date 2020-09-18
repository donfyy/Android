package com.donfyy.a_conflictdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.donfyy.conflict.R;
import com.donfyy.conflict.databinding.FragmentConflictBinding;

public class ConflictFragment extends Fragment {
    FragmentConflictBinding viewDataBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_conflict, container, false);
        String text = "";
        for(int i = 0; i < 100; i++){
            text = text + getArguments().get("index");
        }
        viewDataBinding.textview.setText(text);
        return viewDataBinding.getRoot();
    }

}