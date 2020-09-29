package com.ytzb.chart;

import android.view.MotionEvent;

import com.ytzb.chart.utils.YPointF;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public interface OnChartGestureListener {
    void onSingleTap();

    void onHighlightStart(float xPx, float yPx);

    void onHighlight(float xPx, float yPx);

    void onHighlightStop();

    void onScaleX(MotionEvent event);

    void onXDragStart(MotionEvent event);

    void onXDrag(MotionEvent event, YPointF savedPoint);

    void onXDragStop(MotionEvent event);
}
