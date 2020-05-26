package com.xiangxue.d_innerouterconflict.innerinterceptnolifthand.nolifthand;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xiangxue.d_innerouterconflict.base.CommonLayout;

public class VerContainerView extends CommonLayout {

    public VerContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (currentIndex == 0) {
            return true;
        } else {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    return false;
                default:
                    return true;
            }
        }
    }
}
