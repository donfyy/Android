package com.donfyy.e_conflictfixcases.x5webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewClientExtension;
import com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewCallbackClient;

public class FixedX5Webview extends WebView implements WebViewCallbackClient {
    public FixedX5Webview(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        init();
    }

    public FixedX5Webview(Context arg0) {
        super(arg0);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, View view) {
        return super.super_onTouchEvent(event);
    }

    @Override
    public boolean overScrollBy(int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, boolean b, View view) {
        return super_overScrollBy(i, i1, i2, i3, i4, i5, i6, i7, b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent, View view) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.super_dispatchTouchEvent(motionEvent);
    }

    @Override
    public void computeScroll(View view) {
        super.super_computeScroll();
    }

    @Override
    public void onOverScrolled(int i, int i1, boolean b, boolean b1, View view) {
        if (b) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        super.super_onOverScrolled(i, i1, b, b1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event, View view) {

        return super.super_onInterceptTouchEvent(event);
    }

    @Override
    public void onScrollChanged(int var1, int var2, int var3, int var4, View view) {
        super.super_onScrollChanged(var1, var2, var3, var4);

    }

    @Override
    public void invalidate() {
    }

    private IX5WebViewClientExtension mWebViewClientExtension = new ProxyWebViewClientExtension() {
        @Override
        public boolean onTouchEvent(MotionEvent event, View view) {

            return FixedX5Webview.this.onTouchEvent(event, view);
        }

        // 1
        public boolean onInterceptTouchEvent(MotionEvent ev, View view) {
            return FixedX5Webview.this.onInterceptTouchEvent(ev, view);
        }

        // 3
        public boolean dispatchTouchEvent(MotionEvent ev, View view) {
            return FixedX5Webview.this.dispatchTouchEvent(ev, view);
        }

        // 4
        public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                    int scrollRangeX, int scrollRangeY,
                                    int maxOverScrollX, int maxOverScrollY,
                                    boolean isTouchEvent, View view) {
            return FixedX5Webview.this.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                    scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent, view);
        }

        // 5
        public void onScrollChanged(int l, int t, int oldl, int oldt, View view) {
            FixedX5Webview.this.onScrollChanged(l, t, oldl, oldt, view);
        }

        // 6
        public void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
                                   boolean clampedY, View view) {
            FixedX5Webview.this.onOverScrolled(scrollX, scrollY, clampedX, clampedY, view);
        }

        // 7
        public void computeScroll(View view) {
            FixedX5Webview.this.computeScroll(view);
        }
    };

    private void init() {
        setWebViewCallbackClient(this);
        if (getX5WebViewExtension() != null) {
            getX5WebViewExtension().setWebViewClientExtension(mWebViewClientExtension);
        }
        setVerticalScrollBarEnabled(false);
        setOverScrollMode(WebView.OVER_SCROLL_IF_CONTENT_SCROLLS);
    }
}
