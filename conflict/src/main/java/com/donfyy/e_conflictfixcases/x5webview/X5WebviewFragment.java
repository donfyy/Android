package com.donfyy.e_conflictfixcases.x5webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.donfyy.conflict.R;
import com.donfyy.conflict.databinding.FragmentX5webviewBinding;

public class X5WebviewFragment extends Fragment {
    FragmentX5webviewBinding viewDataBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_x5webview, container, false);
        viewDataBinding.webview.getSettings().setJavaScriptEnabled(true);
        if(getArguments().get("index").equals("0")){
            viewDataBinding.webview.loadUrl("file:///android_asset/demos/310-thumbs-gallery-loop.html");
        }

        return viewDataBinding.getRoot();
    }

}