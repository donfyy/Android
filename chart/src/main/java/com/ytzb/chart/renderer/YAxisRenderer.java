package com.ytzb.chart.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.ytzb.chart.Transformer;
import com.ytzb.chart.Viewport;
import com.ytzb.chart.data.AxisDependency;
import com.ytzb.chart.data.Highlight;
import com.ytzb.chart.data.YAxis;
import com.ytzb.chart.utils.Utils;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public class YAxisRenderer extends AxisRenderer<YAxis> {
    private float[] mBuffer = new float[4];

    public YAxisRenderer(Viewport viewport, YAxis axis, Transformer transformer) {
        super(viewport, axis, transformer);
    }

    @Override
    public void renderAxisLine(Canvas canvas) {
        if (!mAxis.isDrawAxisLine()) {
            return;
        }
        mRenderPaint.setStrokeWidth(mAxis.getAxisLineWidth());
        mRenderPaint.setColor(mAxis.getAxisLineColor());

        if (mAxis.getAxisDependency() == AxisDependency.LEFT) {
            canvas.drawLine(
                    mViewport.contentLeft(),
                    mViewport.contentTop(),
                    mViewport.offsetLeft(),
                    mViewport.contentBottom(),
                    mRenderPaint
            );
        } else {
            canvas.drawLine(
                    mViewport.contentRight(),
                    mViewport.contentTop(),
                    mViewport.contentRight(),
                    mViewport.contentBottom(),
                    mRenderPaint
            );
        }
    }

    @Override
    public void renderGridLines(Canvas canvas) {
        if (mAxis.mEntries == null || mAxis.mEntries.length == 0 || !mAxis.isDrawGridLines()) {
            return;
        }

        mGridLinePaint.setStrokeWidth(mAxis.getGridLineWidth());
        mGridLinePaint.setColor(mAxis.getGridLineColor());
        mGridLinePaint.setPathEffect(mAxis.getGridLinePathEffect());
        mGridLinePaint.setStyle(Paint.Style.STROKE);

        for (float y : mAxis.mEntries) {
            mBuffer[1] = y;
            mTransformer.pointValuesToPixel(mBuffer);

            if (mGridLinePaint.getPathEffect() != null) {
                Path path = getPathBuffer();

                path.moveTo(mViewport.contentLeft(), mBuffer[1]);
                path.quadTo(mViewport.contentLeft() + mViewport.contentWidth() / 2, mBuffer[1], mViewport.contentRight(), mBuffer[1]);

                canvas.drawPath(path, mGridLinePaint);
            } else {
                canvas.drawLine(
                        mViewport.contentLeft(),
                        mBuffer[1],
                        mViewport.contentRight(),
                        mBuffer[1],
                        mGridLinePaint
                );
            }
        }
    }

    @Override
    public void renderAxisValues(Canvas canvas) {
        if (!mAxis.isDrawAxisLabels()) {
            return;
        }
        mAxisLabelPaint.setTextSize(mAxis.getTextSize());
        mAxisLabelPaint.setColor(mAxis.getTextColor());

        int height = Utils.calcTextHeight(mAxisLabelPaint, "A");

        float xPx;
        if (mAxis.getAxisDependency() == AxisDependency.LEFT) {
            xPx = mViewport.contentLeft() + mAxis.getXOffset();
            mAxisLabelPaint.setTextAlign(Paint.Align.LEFT);
        } else {
            xPx = mViewport.contentRight() - mAxis.getXOffset();
            mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);
        }

        for (int i = 0; i < mAxis.mEntryCount; i++) {

            if (i == 0 && !mAxis.isDrawBottomLabel()) {
                continue;
            }

            if (i == mAxis.mEntryCount - 1 && !mAxis.isDrawTopLabel()) {
                continue;
            }

            String label = mAxis.mEntryLabels[i];

            mBuffer[1] = mAxis.mEntries[i];

            mTransformer.pointValuesToPixel(mBuffer);

            float yPx = mBuffer[1];


            switch (mAxis.getLabelPosition()) {
                case TOP_INSIDE:
                    yPx -= mAxis.getYOffset();
                    break;
                case CENTER_INSIDE:
                    yPx += height / 2f;
                    break;
                case BOTTOM_INSIDE:
                    yPx += mAxis.getYOffset() + height;
                    break;
            }

            if (mAxis.isAllLabelsInsideBounds()) {
                if (i == 0) {
                    yPx = mBuffer[1] - mAxis.getYOffset();
                } else if (i == mAxis.mEntryCount - 1) {
                    yPx = mBuffer[1] + mAxis.getYOffset() + height;
                }
            }

            canvas.drawText(label, xPx, yPx, mAxisLabelPaint);
        }
    }

    @Override
    public void renderHighlight(Canvas canvas, Highlight highlight) {
        if (!mViewport.isYInBounds(highlight.getYPx())) {
            return;
        }

        if (mAxis.isLabelsInside()) {
            mHighlightPaint.setTextSize(mAxis.getTextSize());

            String label = mAxis.getAxisFormatter().getFormattedValue(highlight.getY(), mAxis);
            int labelHeight = Utils.calcTextHeight(mHighlightPaint, label);
            int labelWidth = Utils.calcTextWidth(mHighlightPaint, label);
            if (mAxis.getAxisDependency() == AxisDependency.LEFT) {
                mHighlightPaint.setTextAlign(Paint.Align.LEFT);
                mBuffer[0] = mViewport.contentLeft();
                mBuffer[2] = mViewport.contentLeft() + mAxis.getHLabelXOffset() * 2 + labelWidth;
            } else {
                mHighlightPaint.setTextAlign(Paint.Align.RIGHT);
                mBuffer[0] = mViewport.contentRight() - mAxis.getHLabelXOffset() * 2 - labelWidth;
                mBuffer[2] = mViewport.contentRight();
            }
            mBuffer[1] = highlight.getYPx() - labelHeight / 2 - mAxis.getHLabelYOffset();
            mBuffer[3] = highlight.getYPx() + labelHeight / 2 + mAxis.getHLabelYOffset();

            mHighlightPaint.setColor(mAxis.getHLabelBgColor());
            canvas.drawRect(mBuffer[0], mBuffer[1], mBuffer[2], mBuffer[3], mHighlightPaint);

            if (mAxis.getAxisDependency() == AxisDependency.LEFT) {
                mBuffer[0] = mViewport.contentLeft() + mAxis.getHLabelXOffset();
            } else {
                mBuffer[0] = mViewport.contentRight() - mAxis.getHLabelXOffset();
            }
            mBuffer[1] = highlight.getYPx() + labelHeight / 2;

            mHighlightPaint.setColor(mAxis.getHLabelColor());
            canvas.drawText(label, mBuffer[0], mBuffer[1], mHighlightPaint);
        }
    }

}
