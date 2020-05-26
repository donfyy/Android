package com.xiangxue.willremove.intercept.innerintercept;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.xiangxue.willremove.intercept.base.BaseSimpleViewPager;

public class InnerSimpleViewPager extends BaseSimpleViewPager {
    private static final String TAG = "InnerSimpleViewPager";

    public InnerSimpleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.d("xxxxxxxxinin", event.getAction() +"dx:" + event.getX() + " dy:" + event.getY());
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            return false;
        } else {
            return true;
        }
    }
}
