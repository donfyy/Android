package com.donfyy.crowds;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.android.DaggerFragment;

public class L2Fragment extends DaggerFragment {
    @Inject
    AppComponentA mAppComponentA;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_l2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAppComponentA != null) {
            Log.e(this.getClass().getSimpleName(), "mAppComponentA injected!");
        }
    }
}
