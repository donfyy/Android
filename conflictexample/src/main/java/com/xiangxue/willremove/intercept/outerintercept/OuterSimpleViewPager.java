package com.xiangxue.willremove.intercept.outerintercept;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xiangxue.willremove.intercept.base.BaseSimpleViewPager;

public class OuterSimpleViewPager extends BaseSimpleViewPager {
    private static final String TAG = "OuterSimpleViewPager";
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    public OuterSimpleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            }
        }
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted;
    }

}
