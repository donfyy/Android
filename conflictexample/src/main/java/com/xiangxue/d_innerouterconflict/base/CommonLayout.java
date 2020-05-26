package com.xiangxue.d_innerouterconflict.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class CommonLayout extends ViewGroup {
    protected Scroller mScroller;
    protected VelocityTracker mVelocityTracker = VelocityTracker.obtain();

    protected int lastY;
    protected int currentIndex;
    public CommonLayout(Context context) {
        super(context);
        init();
    }

    public CommonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        widthMeasureSpec = modeWidth == MeasureSpec.EXACTLY ?
                widthMeasureSpec : MeasureSpec.makeMeasureSpec(500, MeasureSpec.EXACTLY);
        heightMeasureSpec = modeHeight == MeasureSpec.EXACTLY ?
                heightMeasureSpec : MeasureSpec.makeMeasureSpec(500, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : 500 + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : 500 + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(0, top, child.getMeasuredWidth(), top + child.getMeasuredHeight());
            top = child.getMeasuredHeight();
        }
    }

    protected void init() {
        mScroller = new Scroller(getContext());
    }

    protected void scrollerToPager() {
        int scrollY = mScroller.getFinalY();
        if (scrollY >= getMeasuredHeight() / 2) {
            smoothScrollBy(getMeasuredHeight() - scrollY);
            currentIndex = 1;
        } else {
            smoothScrollBy(-scrollY);
            currentIndex = 0;
        }
    }

    protected void smoothScrollBy(int dy) {
        mScroller.startScroll(0, mScroller.getFinalY(), 0, dy);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                smoothScrollBy(lastY - y);
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(100);
                int speedY = (int) mVelocityTracker.getYVelocity();
                if (speedY < -500) {
                    smoothScrollBy(getMeasuredHeight() - mScroller.getFinalY());
                    currentIndex = 1;
                } else if (speedY > 500) {
                    smoothScrollBy(-mScroller.getFinalY());
                    currentIndex = 0;
                } else {
                    scrollerToPager();
                }
                break;
        }
        this.lastY = y;
        return true;
    }
    public void setLastY(int lastY) {
        this.lastY = lastY;
    }
}
