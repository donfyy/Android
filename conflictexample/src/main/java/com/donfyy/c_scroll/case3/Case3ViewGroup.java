package com.donfyy.c_scroll.case3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

import com.donfyy.c_scroll.case2.Case2ViewGroup;


/**
 * 示例：自定义一个 ViewGroup，包含几个一字排开的子 View，
 * 每个子 View 都与该 ViewGroup 一样大。
 * 通过 VelocityTracker 监控手指滑动速度。
 *
 */
public class Case3ViewGroup extends Case2ViewGroup {

    // 速度监控器
    private VelocityTracker mVelocityTracker;

    public Case3ViewGroup(Context context) {
        super(context);
    }

    public Case3ViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Case3ViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 非滑动状态
    private static final int TOUCH_STATE_REST = 0;
    // 滑动状态
    private static final int TOUCH_STATE_SCROLLING = 1;
    // 表示当前状态
    private int mTouchState = TOUCH_STATE_REST;

    // 上一次事件的位置
    private float mLastMotionX;
    // 触发滚动的最小滑动距离，手指滑动超过该距离才认为是要拖动，防止手抖
    private int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    // 最小滑动速率，手指滑动超过该速度时才会触发翻页
    private static final int VELOCITY_MIN = 600;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        //表示已经开始滑动了，不需要走该 ACTION_MOVE 方法了。
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                break;

            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(mLastMotionX - x);
                //超过了最小滑动距离，就可以认为开始滑动了
                if (xDiff > mTouchSlop) {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return mTouchState != TOUCH_STATE_REST;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        // 速度监控器，监控每一个 event
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        // 触摸点
        final float eventX = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // 如果滚动未结束时按下，则停止滚动
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                // 记录按下位置
                mLastMotionX = eventX;
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指移动的位移
                int deltaX = (int)(eventX - mLastMotionX);
                // 滚动内容，前提是不超出边界
                int targetScrollX = getScrollX() - deltaX;
                if (targetScrollX >= 0 &&
                        targetScrollX <= getWidth() * (CHILD_NUMBER - 1)) {
                    scrollTo(targetScrollX, 0);
                }
                // 记下手指的新位置
                mLastMotionX = eventX;
                break;
            case MotionEvent.ACTION_UP:
                // 计算速度
//                {
//                    mVelocityTracker.computeCurrentVelocity(50);
//                    float velocityX = mVelocityTracker.getXVelocity();
//                    Log.d("chant", "10: velocityX=" + velocityX);
////                    // 小
////                    // molice，cgine 大
//                }
//                {
//                    mVelocityTracker.computeCurrentVelocity(100);
//                    float velocityX = mVelocityTracker.getXVelocity();
//                    Log.d("chant", "UP的时候 velocityX=" + velocityX);
//                    // 大
//                    // molice，cgine 小
//                }

                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityX = mVelocityTracker.getXVelocity();
                if (velocityX > VELOCITY_MIN && canMoveToIndex(getCurrentIndex() - 1)) {
                    // 自动向右继续滑动
                    moveToIndex(getCurrentIndex() - 1);
//                    Log.d("chant", "[UP]: velocityX=" + velocityX + ", lastOne");
                } else if (velocityX < -VELOCITY_MIN && canMoveToIndex(getCurrentIndex() + 1)) {
                    // 自动向左边继续滑动
                    moveToIndex(getCurrentIndex() + 1);
//                    Log.d("chant", "[UP]: velocityX=" + velocityX + ", nextOne");
                } else {
                    // 手指速度不够或不允许再滑
                    int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                    moveToIndex(targetIndex);
//                    Log.d("chant", "[UP]: velocityX=" + velocityX + ", target=" + targetIndex);
                }
                // 回收速度监控器
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                //修正 mTouchState 值
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
        }

        return true;
    }
}
