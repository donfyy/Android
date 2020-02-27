package com.donfyy.crowds.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class UltraViewPager extends ViewPager {
    private boolean mIsEnableInfiniteLoop;

    public UltraViewPager(@NonNull Context context) {
        super(context);
    }

    public UltraViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEnableInfiniteLoop(boolean enableInfiniteLoop) {
        mIsEnableInfiniteLoop = enableInfiniteLoop;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void addOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
    }


    public static class InfiniteLoopWrapperAdapter extends PagerAdapter {
        private PagerAdapter mPagerAdapter;

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return false;
        }
    }


}
