package com.donfyy.crowds.viewpager;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class ZoomOutSlideTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    private float mMinScale = MIN_SCALE;
    private float mMinAlpha = MIN_ALPHA;
    private PageHelper mPageHelper;

    public ZoomOutSlideTransformer() {
    }

    public ZoomOutSlideTransformer(PageHelper pageHelper) {
        mPageHelper = pageHelper;
    }

    public ZoomOutSlideTransformer(float minScale, float minAlpha, PageHelper pageHelper) {
        mMinScale = minScale;
        mMinAlpha = minAlpha;
        mPageHelper = pageHelper;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        ViewPager viewPager = (ViewPager) page.getParent();
//        note:position is reevaluated as previous evaluation not consider the padding left value of ViewPager
        float adjustedPosition = (((float) (page.getLeft() - viewPager.getPaddingLeft() - viewPager.getScrollX())))
                / (viewPager.getWidth() - viewPager.getPaddingLeft() - viewPager.getPaddingRight());

        if (mPageHelper != null && page.getLeft() == 0) {
            int adapterPositionOfView = mPageHelper.getAdapterPositionOfView(page);
            adjustedPosition = Integer.compare(adapterPositionOfView, viewPager.getCurrentItem());
        }

        int height = viewPager.getHeight() - viewPager.getPaddingTop() - viewPager.getPaddingBottom();
        int width = viewPager.getWidth() - viewPager.getPaddingLeft() - viewPager.getPaddingRight();

        float scaleFactor = Math.max(mMinScale, 1 - Math.abs(adjustedPosition));

        page.setPivotX(0.5f * width);
        page.setPivotY(0.5f * height);

        page.setScaleX(scaleFactor);
        page.setScaleY(scaleFactor);

        float horizontalMargin = width * (1 - scaleFactor) / 2;

        if (adjustedPosition < 0) {
            page.setTranslationX(horizontalMargin / 2);
        } else {
            page.setTranslationX(-horizontalMargin / 2);
        }

        page.setAlpha(mMinAlpha + (scaleFactor - mMinScale) / (1 - mMinScale) * (1 - mMinAlpha));

    }
}
