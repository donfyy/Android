package com.donfyy.e_conflictfixcases.webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.donfyy.conflict.R;
import com.donfyy.conflict.databinding.FragmentWebviewBinding;

public class WebviewFragment extends Fragment {
    FragmentWebviewBinding viewDataBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false);
        viewDataBinding.webview.getSettings().setJavaScriptEnabled(true);
        if(getArguments().get("index").equals("0")){
            viewDataBinding.webview.loadUrl("file:///android_asset/demos/230-effect-cube.html");
        }

        return viewDataBinding.getRoot();
    }

}