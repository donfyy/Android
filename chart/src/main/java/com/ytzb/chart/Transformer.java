package com.ytzb.chart;

import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public class Transformer implements Cloneable {
    private Matrix mOffsetMatrix = new Matrix();
    private Matrix mValueToPxMatrix = new Matrix();
    private Matrix mPxToValueMatrixBuffer = new Matrix();

    private Viewport mViewport;

    public Transformer(Viewport viewport) {
        mViewport = viewport;
    }

    public void prepareOffsetMatrix() {
        mOffsetMatrix.reset();

        mOffsetMatrix.postTranslate(mViewport.offsetLeft(), mViewport.contentHeight() + mViewport.offsetTop());
    }

    public void prepareValueToPxMatrix(float xChartMin, float deltaX, float deltaY, float yChartMin) {
        float sx = mViewport.contentWidth() / deltaX;

        if (Float.isInfinite(sx)) {
            sx = 0;
        }
        float sy = mViewport.contentHeight() / deltaY;
        if (Float.isInfinite(sy)) {
            sy = 0;
        }

        mValueToPxMatrix.reset();
        mValueToPxMatrix.postTranslate(-xChartMin, -yChartMin);
        mValueToPxMatrix.postScale(sx, -sy);
    }

    public void pixelsToValue(float[] pts) {
        Matrix buffer = mPxToValueMatrixBuffer;
        mOffsetMatrix.invert(buffer);
        buffer.mapPoints(pts);

        mValueToPxMatrix.invert(buffer);
        buffer.mapPoints(pts);
    }

    public void pointValuesToPixel(float[] pts) {
        mValueToPxMatrix.mapPoints(pts);
        mOffsetMatrix.mapPoints(pts);
    }

    public void pointValuesToPixel(RectF rectF) {
        mValueToPxMatrix.mapRect(rectF);
        mOffsetMatrix.mapRect(rectF);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Transformer clone = (Transformer) super.clone();

        clone.mPxToValueMatrixBuffer = new Matrix();

        clone.mOffsetMatrix = new Matrix();
        clone.mOffsetMatrix.set(mOffsetMatrix);

        clone.mValueToPxMatrix = new Matrix();
        clone.mValueToPxMatrix.set(mValueToPxMatrix);
        return clone;
    }

    public Transformer freeze() {
        try {
            return (Transformer) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
