package com.donfyy.d_innerouterconflict.outerinterceptlifthand.lifthand;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donfyy.d_innerouterconflict.base.CommonLayout;

public class VerContainerView2 extends CommonLayout {
    private MyListView2 mListView;

    public VerContainerView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mListView = (MyListView2) getChildAt(1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (currentIndex == 0) {
            return true;
        } else {
            int y = (int) ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    this.lastY = y;
                    return false;
                case MotionEvent.ACTION_MOVE:
                    return mListView.isTop() && (y - lastY) > 0;
                default:
                    return false;
            }
        }
    }
}
