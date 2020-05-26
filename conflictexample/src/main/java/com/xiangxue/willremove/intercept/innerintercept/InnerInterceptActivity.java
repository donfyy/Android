package com.xiangxue.willremove.intercept.innerintercept;

import com.xiangxue.conflict.R;
import com.xiangxue.willremove.intercept.base.BaseInterceptActivity;

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
