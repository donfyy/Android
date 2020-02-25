package com.donfyy.crowds.viewpager;

import android.view.View;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;

import androidx.viewpager.widget.ViewPager;

public class ZoomOutSlideTransformer extends ABaseTransformer {
    private static final float MIN_SCALE = 0.85f;

    @Override
    protected boolean isPagingEnabled() {
        return true;
    }

    @Override
    protected boolean hideOffscreenPages() {
        return false;
    }

    @Override
    protected void onTransform(View page, float position) {
        ViewPager viewPager = (ViewPager) page.getParent();
        float adjustedPosition = (((float) (page.getLeft() - viewPager.getPaddingLeft() - viewPager.getScrollX())))
                / (page.getWidth());

//        TextView textView = page.findViewById(R.id.number);
//        if ("1".equals(textView.getText())) {
//            Log.e(ZoomOutSlideTransformer.class.getSimpleName(), " adjustedPosition:" + adjustedPosition
//            + " translationX:" + page.getTranslationX());
//        }
        int height = page.getHeight();
        int width = page.getWidth();

        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(adjustedPosition));

        page.setPivotX(0.5f * width);
        page.setPivotY(0.5f * height);

        page.setScaleX(scaleFactor);
        page.setScaleY(scaleFactor);

        float verticalMargin = height * (1 - scaleFactor) / 2;
        float horizontalMargin = width * (1 - scaleFactor) / 2;

        if (adjustedPosition < 0) {
            page.setTranslationX(horizontalMargin / 2);
        } else {
            page.setTranslationX(-horizontalMargin / 2);
        }

    }
}
