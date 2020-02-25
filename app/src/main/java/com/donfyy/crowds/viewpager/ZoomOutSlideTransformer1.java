package com.donfyy.crowds.viewpager;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class ZoomOutSlideTransformer1 implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        ViewPager viewPager = (ViewPager) page.getParent();
//        note:position is reevaluated as previous evaluation not consider the padding left value of ViewPager
        float adjustedPosition = (((float) (page.getLeft() - viewPager.getPaddingLeft() - viewPager.getScrollX())))
                / (page.getWidth());

        int height = page.getHeight();
        int width = page.getWidth();

        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(adjustedPosition));

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

    }
}
