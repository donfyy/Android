package com.donfyy.c_scroll.case2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Scroller;

import com.donfyy.c_scroll.case1.Case1ViewGroup;


/**
 * 示例：自定义一个 ViewGroup，包含几个一字排开的子 View，
 * 每个子 View 都与该 ViewGroup 一样大。
 * 通过 Scroller 实现滚动。
 * 调用 moveToIndex 方法会触发 Scroller 的 startScroller，开始动画，并使 View 失效。
 * 并在 computeScroll 方法中判断动画是否在进行，进而计算当前滚动位置，并触发下一次 View 失效。
 */
public class Case2ViewGroup extends Case1ViewGroup {

    // 滚动器
    protected Scroller mScroller;

    public Case2ViewGroup(Context context) {
        super(context);
        initScroller();
    }

    public Case2ViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScroller();
    }

    public Case2ViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScroller();
    }

    private void initScroller() {
        mScroller = new Scroller(getContext());
    }

    /**
     * 通过动画滚动到第几个子 View
     * @param targetIndex 要移动到第几个子 View
     */
    @Override
    public void moveToIndex(int targetIndex) {
        if (!canMoveToIndex(targetIndex)) {
            return;
        }
        mScroller.startScroll(
                getScrollX(), getScrollY(),
                targetIndex * getWidth() - getScrollX(), getScrollY());
        mCurrentIndex = targetIndex;
        invalidate();
    }

    public void stopMove() {
        if (!mScroller.isFinished()) {
            int currentX = mScroller.getCurrX();
            int targetIndex = (currentX + getWidth() / 2) / getWidth();
            mScroller.abortAnimation();
            this.scrollTo(targetIndex * getWidth(), 0);
            mCurrentIndex = targetIndex;
        }
    }

    /**
     * 在 ViewGroup.dispatchDraw() -> ViewGroup.drawChild() -> View.draw(Canvas,ViewGroup,long) 时被调用
     * 任务：计算 mScrollX & mScrollY 应有的值，然后调用scrollTo/scrollBy
     */
    @Override
    public void computeScroll() {
        boolean isNotFinished = mScroller.computeScrollOffset();
        if (isNotFinished) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

}
