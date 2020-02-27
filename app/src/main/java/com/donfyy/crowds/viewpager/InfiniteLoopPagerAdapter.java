package com.donfyy.crowds.viewpager;

import android.database.DataSetObserver;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Example:
 *         ViewPager viewPager = view.findViewById(R.id.viewPager);
 *         PagerAdapterExample underlyingPagerAdapter = new PagerAdapterExample();
 *         InfiniteLoopPagerAdapter infiniteLoopPagerAdapter = new InfiniteLoopPagerAdapter(underlyingPagerAdapter);
 *         infiniteLoopPagerAdapter.assemble(viewPager);
 */
public class InfiniteLoopPagerAdapter extends PagerAdapter implements PageHelper {

    private final PagerAdapter mUnderlyingPagerAdapter;
    private final int mHalfDuplicatedCount;
    private ArrayMap<View, Integer> mPositionMap = new ArrayMap<>();
    private ViewPager mViewPager;

    public InfiniteLoopPagerAdapter(PagerAdapter underlyingPagerAdapter) {
        this(underlyingPagerAdapter, 2);
    }

    public InfiniteLoopPagerAdapter(PagerAdapter underlyingPagerAdapter, int halfDuplicatedCount) {
        mUnderlyingPagerAdapter = underlyingPagerAdapter;
        mHalfDuplicatedCount = halfDuplicatedCount;
        underlyingPagerAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                notifyDataSetChanged();
            }
        });
    }

    public void assemble(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.setAdapter(this);
        mViewPager.addOnPageChangeListener(new InfiniteLoopPageListener(mViewPager, getFirstItemPosition()));
        mViewPager.setCurrentItem(getFirstItemPosition(), false);
    }

    private int getDataPosition(int position) {
        if (position <= 1) {
            return position + mUnderlyingPagerAdapter.getCount() - mHalfDuplicatedCount;
        } else if (position >= getCount() - mHalfDuplicatedCount) {
            return position - getCount() + mHalfDuplicatedCount;
        } else {
            return position - mHalfDuplicatedCount;
        }
    }

    public int getHalfDuplicatedCount() {
        return mHalfDuplicatedCount;
    }

    public int getFirstItemPosition() {
        if (getCount() >= 3) {
            return mHalfDuplicatedCount;
        }

        return 0;
    }

    public int getUnderlyingCount() {
        return mUnderlyingPagerAdapter.getCount();
    }

    public PagerAdapter getUnderlyingPagerAdapter() {
        return mUnderlyingPagerAdapter;
    }

    @Override
    public int getCount() {
        if (mUnderlyingPagerAdapter.getCount() >= 3) {
            return mUnderlyingPagerAdapter.getCount() + (mHalfDuplicatedCount << 1);
        }
        return mUnderlyingPagerAdapter.getCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return mUnderlyingPagerAdapter.isViewFromObject(view, object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int childCount = container.getChildCount();
        Object o = mUnderlyingPagerAdapter.instantiateItem(container, getDataPosition(position));
        if (container.getChildCount() > 0) {
            View child = container.getChildAt(childCount);
            mPositionMap.put(child, position);
        }
        return o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mUnderlyingPagerAdapter.destroyItem(container, getDataPosition(position), object);
    }

    @Override
    public int getAdapterPositionOfView(View view) {
        Integer integer = mPositionMap.get(view);
        return integer != null ? integer : 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mViewPager.setCurrentItem(getFirstItemPosition(), false);
    }
}
