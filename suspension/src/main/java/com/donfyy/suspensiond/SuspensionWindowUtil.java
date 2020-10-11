package com.donfyy.suspensiond;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;


public class SuspensionWindowUtil {

    private SuspensionView suspensionView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Context mContext;
    private ValueAnimator valueAnimator;
    private int direction;
    private final int LEFT = 0;
    private final int RIGHT = 1;
    private GestureDetector mGestureDetector;

    //私有化构造函数
    public SuspensionWindowUtil(Context context) {
        mContext = context;
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mGestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                onActionDown(e);
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                context.startActivity(new Intent(context, MainActivity.class));
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                onActionMove(e2);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    public void showSuspensionView() {

        // 创建suspensionview
        suspensionView = new SuspensionView(mContext);

        // 创建一个布局参数
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width =suspensionView.width;
        layoutParams.height = suspensionView.height;

        // gravity
//        layoutParams.x += ScreenSizeUtil.getScreenWidth();
//        layoutParams.y += ScreenSizeUtil.getScreenHeight() - ScreenSizeUtil.dp2px(10);
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        // 设置window type
        // 悬浮窗口
        // 系统级别 type
        // 应用级别
//        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else  {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //flags
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        layoutParams.format = PixelFormat.RGBA_8888;

        suspensionView.setOnTouchListener(touchListener);

        //WindowManager 通过WindowManager添加view
        windowManager.addView(suspensionView,layoutParams);


    }


    public void hideSuspensionView() {
        if(suspensionView !=null){
            windowManager.removeView(suspensionView);
            stopAnim();
        }
    }

    float startX;
    float startY;
    float moveX;
    float moveY;
    View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                onActionUp(event);
            }
            return mGestureDetector.onTouchEvent(event);
        }

    };

    private boolean onActionUp(MotionEvent event) {
        //判断松手时View的横坐标是靠近屏幕哪一侧，将View移动到依靠屏幕
        float endX = event.getRawX();
        float endY = event.getRawY();
        if (endX < ScreenSizeUtil.getScreenWidth() / 2) {
            direction = LEFT;
            endX = 0;
        } else {
            direction = RIGHT;
            endX = ScreenSizeUtil.getScreenWidth() - suspensionView.width;
        }
        if (moveX != startX) {
            starAnim((int) moveX, (int) endX, direction);
        }
        //如果初始落点与松手落点的坐标差值超过5个像素，则拦截该点击事件
        //否则继续传递，将事件交给OnClickListener函数处理
        if (Math.abs(startX - moveX) > 5) {
            return true;
        }
        return false;
    }

    private void onActionMove(MotionEvent event) {
        float x = event.getRawX() - moveX;
        float y = event.getRawY() - moveY;
        //计算偏移量，刷新视图
        layoutParams.x += x;
        layoutParams.y += y;
        windowManager.updateViewLayout(suspensionView, layoutParams);
        moveX = event.getRawX();
        moveY = event.getRawY();
    }

    private void onActionDown(MotionEvent event) {
        startX = event.getRawX();
        startY = event.getRawY();

        moveX = event.getRawX();
        moveY = event.getRawY();
    }

    private void starAnim(int startX, int endX, final int direction) {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
        valueAnimator = ValueAnimator.ofInt(startX, endX);
        valueAnimator.setDuration(500);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (direction == LEFT) {
                    layoutParams.x = (int) animation.getAnimatedValue() - suspensionView.width / 2;
                } else {
                    layoutParams.x = (int) animation.getAnimatedValue();
                }
                if (suspensionView != null) {
                    windowManager.updateViewLayout(suspensionView, layoutParams);
                }
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private void stopAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }
}
