package com.donfyy.a_conflictdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class TestHorizontalScrollView extends HorizontalScrollView {
    public TestHorizontalScrollView(Context context) {
        super(context);
    }

    public TestHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

}
