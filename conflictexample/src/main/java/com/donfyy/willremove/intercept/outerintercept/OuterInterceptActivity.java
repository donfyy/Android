package com.donfyy.willremove.intercept.outerintercept;

import com.donfyy.conflict.R;
import com.donfyy.willremove.intercept.base.BaseInterceptActivity;

public class OuterInterceptActivity extends BaseInterceptActivity {

    @Override
    protected String getTag() {
        return "OuterInterceptActivity";
    }

    @Override
    protected int getViewPagerViewLayout() {
        return R.layout.outer_layout;
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_outer_intercept;
    }
}
