package com.donfyy.e_conflictfixcases.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.view.MotionEvent;

import com.donfyy.b_viewarchitecture.views.Static;

public class FixedWebview extends WebView {
    public static final String TAG = "FixedWebview";

    public FixedWebview(Context context) {
        super(context);
    }

    public FixedWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FixedWebview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FixedWebview(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Static.printMotionEventAction(TAG, "dispatchTouchEvent", event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (clampedX) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }
}
