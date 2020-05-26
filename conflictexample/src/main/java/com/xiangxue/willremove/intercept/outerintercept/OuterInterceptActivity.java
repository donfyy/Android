package com.xiangxue.willremove.intercept.outerintercept;

import com.xiangxue.conflict.R;
import com.xiangxue.willremove.intercept.base.BaseInterceptActivity;

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
