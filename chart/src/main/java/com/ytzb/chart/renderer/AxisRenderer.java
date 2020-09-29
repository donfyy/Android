package com.ytzb.chart.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.ytzb.chart.IAxisLabelsComputer;
import com.ytzb.chart.Transformer;
import com.ytzb.chart.Viewport;
import com.ytzb.chart.data.Axis;
import com.ytzb.chart.data.Highlight;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public abstract class AxisRenderer<T extends Axis> extends Renderer {
    protected T mAxis;
    protected Transformer mTransformer;
    protected Paint mAxisLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mGridLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mPathBuffer;

    public AxisRenderer(Viewport viewport, T axis, Transformer transformer) {
        super(viewport);
        mAxis = axis;
        mTransformer = transformer;
    }

    public void computeAxis(float min, float max) {
        IAxisLabelsComputer axisLabelsComputer = mAxis.getAxisLabelsComputer();
        if (axisLabelsComputer != null) {
            axisLabelsComputer.compute(min, max, mAxis);
            return;
        }
        float interval = (max - min) / (mAxis.mEntryCount - 1);

        if (interval <= 0) {
            mAxis.mEntries = null;
            return;
        }

        if (mAxis.mEntries == null || mAxis.mEntries.length != mAxis.mEntryCount) {
            mAxis.mEntries = new float[mAxis.mEntryCount];
            mAxis.mEntryLabels = new String[mAxis.mEntryCount];
        }

        for (int i = 0; i < mAxis.mEntryCount; i++) {
            float v = min + i * interval;
            mAxis.mEntries[i] = v;
            mAxis.mEntryLabels[i] = mAxis.getAxisFormatter().getFormattedValue(v, mAxis);
        }
    }

    public Paint getAxisLabelPaint() {
        return mAxisLabelPaint;
    }

    public abstract void renderAxisLine(Canvas canvas);

    public abstract void renderGridLines(Canvas canvas);

    public abstract void renderAxisValues(Canvas canvas);

    public abstract void renderHighlight(Canvas canvas, Highlight highlight);

    protected Path getPathBuffer() {
        if (mPathBuffer == null) {
            mPathBuffer = new Path();
        }
        mPathBuffer.reset();
        return mPathBuffer;
    }
}
