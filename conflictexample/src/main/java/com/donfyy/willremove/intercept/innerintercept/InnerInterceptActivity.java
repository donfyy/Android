package com.donfyy.willremove.intercept.innerintercept;

import com.donfyy.conflict.R;
import com.donfyy.willremove.intercept.base.BaseInterceptActivity;

public class InnerInterceptActivity extends BaseInterceptActivity {

    @Override
    protected String getTag() {
        return "InnerInterceptActivity";
    }

    @Override
    protected int getViewPagerViewLayout() {
        return R.layout.inner_layout;
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_inner_intercept;
    }
}
