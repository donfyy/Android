package com.donfyy.willremove.intercept.innerintercept;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;


public class InnerListView extends ListView {
    private static final String TAG = "InnerListView";

    // 分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    public InnerListView(Context context) {
        super(context);
    }

    public InnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //Solution 1
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.d(TAG, "dx:" + deltaX + " dy:" + deltaY);
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            }
        }

        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(event);
    }

    // Solution 2, onInterceptTouchEvent is called only if mFirstTouchTarget is not null for ACTION_MOVE touch event.
    //             InnerListView must have child to accept ACTION_DOWN touch event,
    //             thus InnerListView's mFirstTouchTarget will not be null. Then latter
    //             ACTION_MOVE touch events will pass to onInterceptTouchEvent method
    //
    //            // Check for interception.
    //            final boolean intercepted;
    //            if (actionMasked == MotionEvent.ACTION_DOWN
    //                    || mFirstTouchTarget != null) {
    //                final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
    //                if (!disallowIntercept) {
    //                    intercepted = onInterceptTouchEvent(ev);
    //                    ev.setAction(action); // restore action in case it was changed
    //                } else {
    //                    intercepted = false;
    //                }
    //            }
    //
 /*   @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.d(TAG, "dx:" + deltaX + " dy:" + deltaY);
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            }
        }

        mLastX = x;
        mLastY = y;
        return super.onInterceptTouchEvent(event);
    }*/
}
