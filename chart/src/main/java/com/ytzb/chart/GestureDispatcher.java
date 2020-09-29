package com.ytzb.chart;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ytzb.chart.data.Data;
import com.ytzb.chart.utils.YPointF;

/**
 * Created by xinxin.wang on 18/4/23.
 */

class GestureDispatcher extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    private static final String TAG = GestureDispatcher.class.getName();

    private GestureDetector mGestureDetector;

    private Gesture mLastGesture = Gesture.NONE;

    private float mDragDistanceTrigger;

    // states
    protected static final int NONE = 0;
    protected static final int X_DRAG = 1;
    protected static final int X_ZOOM = 2;
    protected static final int HIGHLIGHT = 3;

    private int mTouchMode = NONE;

    private YPointF mSavedPoint = YPointF.getInstance(0, 0);

    private Chart mChart;

    GestureDispatcher(Chart chart) {
        mGestureDetector = new GestureDetector(chart.getContext(), this);
        mDragDistanceTrigger = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, chart.getResources().getDisplayMetrics());
        mChart = chart;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mTouchMode == NONE || mTouchMode == HIGHLIGHT) {
            mGestureDetector.onTouchEvent(event);
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mSavedPoint.x = event.getX();
                mSavedPoint.y = event.getY();
                if (mChart.isHighlight() && mTouchMode == NONE) {
                    startHighlight(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mTouchMode == X_DRAG) {
                    dragX(event);
                } else if (mTouchMode == HIGHLIGHT) {
                    highlight(event);
                } else if (mTouchMode == NONE) {
                    if (Math.abs(event.getX() - mSavedPoint.x) > mDragDistanceTrigger && mChart.isEnableXDrag()) {
                        startDragX(event);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                final int touchMode = mTouchMode;
                if (touchMode == X_DRAG) {
                    stopDragX(event);
                } else if (touchMode == HIGHLIGHT) {
                    stopHighlight();
                }


                mTouchMode = NONE;
                break;
        }

        return true;
    }

    private void stopHighlight() {
        mChart.stopHighlight();
        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onHighlightStop();
        }
    }

    private void stopDragX(MotionEvent event) {
        mLastGesture = Gesture.X_DRAG;
        mChart.stopDragX(event);

        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onXDragStop(event);
        }
    }

    private void startDragX(MotionEvent event) {
        Data data = mChart.getData();
        if (data == null) {
            return;
        }

        mTouchMode = X_DRAG;
        mChart.getParent().requestDisallowInterceptTouchEvent(true);
        mChart.startDragX(event);

        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onXDragStart(event);
        }
    }

    private void highlight(MotionEvent event) {
        mChart.highlight(event.getX(), event.getY());

        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onHighlight(event.getX(), event.getY());
        }
    }

    private void dragX(MotionEvent event) {
        mChart.dragX(event, mSavedPoint);

        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onXDrag(event, mSavedPoint);
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mLastGesture = Gesture.SINGLE_TAP;
        mChart.singleTapUp();

        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onSingleTap();
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (mTouchMode != NONE) {
            return;
        }
        startHighlight(e);
    }

    private void startHighlight(MotionEvent e) {
        mLastGesture = Gesture.HIGHLIGHT;
        mTouchMode = HIGHLIGHT;
        mChart.getParent().requestDisallowInterceptTouchEvent(true);

        float x = e.getX();
        float y = e.getY();

        mChart.startHighlight(x, y);

        OnChartGestureListener l = mChart.getOnChartGestureListener();
        if (l != null) {
            l.onHighlightStart(x, y);
        }

        mChart.invalidate();
    }
}
