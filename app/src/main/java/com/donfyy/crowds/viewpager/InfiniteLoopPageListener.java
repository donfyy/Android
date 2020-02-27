package com.donfyy.crowds.viewpager;

import android.util.Log;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

class InfiniteLoopPageListener implements ViewPager.OnPageChangeListener {
    private int mPositionBeforeScroll;
    private ViewPager viewPager;

    InfiniteLoopPageListener(ViewPager viewPager, int initialPosition) {
        this.viewPager = viewPager;
        mPositionBeforeScroll = initialPosition;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (ViewPager.SCROLL_STATE_IDLE == state) {
            PagerAdapter adapter = viewPager.getAdapter();
            if (adapter instanceof InfiniteLoopPagerAdapter && viewPager.getCurrentItem() != mPositionBeforeScroll) {
                InfiniteLoopPagerAdapter infiniteLoopPagerAdapter = (InfiniteLoopPagerAdapter) adapter;

                int currentItem = viewPager.getCurrentItem();
                mPositionBeforeScroll = currentItem;

                int relativePosition = (currentItem - infiniteLoopPagerAdapter.getHalfDuplicatedCount()) % infiniteLoopPagerAdapter.getUnderlyingCount();
                int correctPosition;

                if (relativePosition < 0) {
                    correctPosition = infiniteLoopPagerAdapter.getHalfDuplicatedCount() + relativePosition + infiniteLoopPagerAdapter.getUnderlyingCount();
                } else {
                    correctPosition = infiniteLoopPagerAdapter.getHalfDuplicatedCount() + relativePosition;
                }

                if (currentItem != correctPosition) {
                    Log.e(ViewPagerFragment.class.getSimpleName(), "currentPosition:" + currentItem + "  correctPosition:" + correctPosition);
                    viewPager.setCurrentItem(correctPosition, false);
                }
            }
        }
    }

}
