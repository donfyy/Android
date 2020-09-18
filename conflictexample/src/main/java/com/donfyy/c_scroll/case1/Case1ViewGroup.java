package com.donfyy.c_scroll.case1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 示例：自定义一个 ViewGroup，包含几个一字排开的子 View，
 * 每个子 View 都与该 ViewGroup 一样大。
 * 调用 moveToIndex 方法会调用 scrollTo 方法，从而瞬时滚动到某一位置
 */
public class Case1ViewGroup extends ViewGroup {

    public static final int CHILD_NUMBER = 6;
    protected int mCurrentIndex = 0;

    public Case1ViewGroup(Context context) {
        super(context);
        initChildView();
    }

    public Case1ViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChildView();
    }

    public Case1ViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChildView();
    }

    private void initChildView() {
        // 添加几个子 View
        for (int i = 0; i < CHILD_NUMBER; i++) {
            TextView child = new TextView(getContext());
            int color;
            switch (i % 3) {
                case 0:
                    color = 0xffcc6666;
                    break;
                case 1:
                    color = 0xffcccc66;
                    break;
                case 2:
                default:
                    color = 0xff6666cc;
                    break;
            }
            child.setBackgroundColor(color);
            child.setGravity(Gravity.CENTER);
            child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 46);
            child.setTextColor(0x80ffffff);
            child.setText(String.valueOf(i));
            addView(child);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        // 每个子 View 都与自己一样大
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View childView = getChildAt(i);
            childView.measure(
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 子 View 一字排开
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View childView = getChildAt(i);
            childView.layout(getWidth() * i, 0, getWidth() * (i + 1), b - t);
        }
    }

    /**
     * 瞬时滚动到第几个子 View
     * @param targetIndex 要移动到第几个子 View
     */
    public void moveToIndex(int targetIndex) {
        if (!canMoveToIndex(targetIndex)) {
            return;
        }
        scrollTo(targetIndex * getWidth(), getScrollY());
        mCurrentIndex = targetIndex;
        invalidate();
    }

    /**
     * 判断移动的子 View 下标是否合法
     * @param index 要移动到第几个子 View
     * @return index 是否合法
     */
    public boolean canMoveToIndex(int index) {
        return index < CHILD_NUMBER && index >= 0;
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

}
