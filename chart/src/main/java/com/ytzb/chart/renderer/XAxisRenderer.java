package com.ytzb.chart.renderer;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.ytzb.chart.Transformer;
import com.ytzb.chart.Viewport;
import com.ytzb.chart.data.Highlight;
import com.ytzb.chart.data.XAxis;
import com.ytzb.chart.utils.Utils;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public class XAxisRenderer extends AxisRenderer<XAxis> {

    private float[] mBuffer = new float[4];

    private float mCenterYPxOfLabels;

    public XAxisRenderer(Viewport viewport, XAxis axis, Transformer transformer) {
        super(viewport, axis, transformer);
    }

    @Override
    public void renderAxisLine(Canvas canvas) {
        if (!mAxis.isDrawAxisLine()) {
            return;
        }

        mRenderPaint.setStrokeWidth(mAxis.getAxisLineWidth());
        mRenderPaint.setColor(mAxis.getAxisLineColor());

        canvas.drawLine(
                mViewport.contentLeft(),
                mViewport.contentBottom(),
                mViewport.contentRight(),
                mViewport.contentBottom(),
                mRenderPaint
        );
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

        for (float x : mAxis.mEntries) {
            mBuffer[0] = x;
            mTransformer.pointValuesToPixel(mBuffer);

            if (mBuffer[0] <= mViewport.contentLeft() + mAxis.getGridLineOffset()) {
                continue;
            }

            if (mBuffer[0] >= mViewport.contentRight() - mAxis.getGridLineOffset()) {
                continue;
            }
            if (mGridLinePaint.getPathEffect() == null) {
                canvas.drawLine(
                        mBuffer[0],
                        mViewport.contentTop(),
                        mBuffer[0],
                        mViewport.contentBottom(),
                        mGridLinePaint
                );
            } else {
                Path path = getPathBuffer();
                path.moveTo(mBuffer[0], mViewport.contentTop());
                path.quadTo(mBuffer[0], mViewport.contentTop() + mViewport.contentHeight() / 2, mBuffer[0], mViewport.contentBottom());

                canvas.drawPath(path, mGridLinePaint);
            }
        }
    }

    @Override
    public void renderAxisValues(Canvas canvas) {
        if (mAxis.mEntries == null || mAxis.mEntries.length == 0 || !mAxis.isDrawAxisLabels()) {
            return;
        }

        mAxisLabelPaint.setTextSize(mAxis.getTextSize());
        mAxisLabelPaint.setColor(mAxis.getTextColor());

        int height = Utils.calcTextHeight(mAxisLabelPaint, "A");

        for (int i = 0; i < mAxis.mEntries.length; i++) {
            float x = mAxis.mEntries[i];
            String label = mAxis.mEntryLabels[i];
            mBuffer[0] = x;
            mTransformer.pointValuesToPixel(mBuffer);
            switch (mAxis.getLabelPosition()) {
                case LEFT_OUTSIDE:
                    mAxisLabelPaint.setTextAlign(Paint.Align.LEFT);
                    break;
                case CENTER_OUTSIDE:
                    mAxisLabelPaint.setTextAlign(Paint.Align.CENTER);
                    break;
                case RIGHT_OUTSIDE:
                    mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);
                    break;
            }
            if (mAxis.isAllLabelsInsideBounds()) {
                if (i == 0) {
                    int width = Utils.calcTextWidth(mAxisLabelPaint, label);

                    if (mBuffer[0] - width / 2 < mViewport.contentLeft()) {
                        mBuffer[0] = mViewport.contentLeft();
                        mAxisLabelPaint.setTextAlign(Paint.Align.LEFT);
                    }
                } else if (i == mAxis.mEntries.length - 1) {

                    int width = Utils.calcTextWidth(mAxisLabelPaint, label);

                    if (mBuffer[0] + width / 2 > mViewport.contentRight()) {
                        mBuffer[0] = mViewport.contentRight();
                        mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);
                    }
                }
            }
            canvas.drawText(label, mBuffer[0], mViewport.contentBottom() + mAxis.getYOffset() + height, mAxisLabelPaint);
        }

        mCenterYPxOfLabels = mViewport.contentBottom() + mAxis.getYOffset() + height / 2;
    }

    @Override
    public void renderHighlight(Canvas canvas, Highlight highlight) {
        if (!mAxis.isDrawAxisLabels()) {
            return;
        }

        mHighlightPaint.setTextSize(mAxis.getTextSize());

        String label = mAxis.getXAxisFormatter().getFormattedValue(mAxis, highlight.getEntriesMap());
        int height = Utils.calcTextHeight(mHighlightPaint, label);
        int width = Utils.calcTextWidth(mHighlightPaint, label);

        int halfWidth = width / 2;
        int halfHeight = height / 2;
        mBuffer[0] = highlight.getXPx() - halfWidth - mAxis.getHLabelXOffset();
        mBuffer[1] = mCenterYPxOfLabels - halfHeight - mAxis.getHLabelYOffset();
        mBuffer[2] = highlight.getXPx() + halfWidth + mAxis.getHLabelXOffset();
        mBuffer[3] = mCenterYPxOfLabels + halfHeight + mAxis.getHLabelYOffset();

        if (mBuffer[0] < mViewport.contentLeft()) {
            float offset = mViewport.contentLeft() - mBuffer[0];
            mBuffer[0] += offset;
            mBuffer[2] += offset;
        } else if (mBuffer[2] > mViewport.contentRight()) {
            float offset = mBuffer[2] - mViewport.contentRight();
            mBuffer[0] -= offset;
            mBuffer[2] -= offset;
        }

        mHighlightPaint.setColor(mAxis.getHLabelBgColor());
        canvas.drawRect(mBuffer[0], mBuffer[1], mBuffer[2], mBuffer[3], mHighlightPaint);

        mBuffer[0] = highlight.getXPx();
        mBuffer[1] = mCenterYPxOfLabels + halfHeight;

        if (mBuffer[0] - halfWidth < mViewport.contentLeft()) {
            mBuffer[0] = mViewport.contentLeft() + mAxis.getHLabelXOffset();
            mHighlightPaint.setTextAlign(Paint.Align.LEFT);
        } else if (mBuffer[0] + halfWidth > mViewport.contentRight()) {
            mBuffer[0] = mViewport.contentRight() - mAxis.getHLabelXOffset();
            mHighlightPaint.setTextAlign(Paint.Align.RIGHT);
        } else {
            mHighlightPaint.setTextAlign(Paint.Align.CENTER);
        }

        mHighlightPaint.setColor(mAxis.getHLabelColor());
        canvas.drawText(label, mBuffer[0], mBuffer[1], mHighlightPaint);
    }
}
