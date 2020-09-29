package com.ytzb.chart.data;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public class YAxis extends Axis {
    private AxisDependency mAxisDependency;

    private boolean mIsDrawBottomLabel;
    private boolean mIsDrawTopLabel;

    private boolean mIsSymmetrical;

    public YAxis(AxisDependency axisDependency) {
        mAxisDependency = axisDependency;
    }

    private YAxisLabelPosition mLabelPosition = YAxisLabelPosition.CENTER_INSIDE;

    public AxisDependency getAxisDependency() {
        return mAxisDependency;
    }

    public YAxisLabelPosition getLabelPosition() {
        return mLabelPosition;
    }

    public void setLabelPosition(YAxisLabelPosition labelPosition) {
        mLabelPosition = labelPosition;
    }

    public boolean isDrawBottomLabel() {
        return mIsDrawBottomLabel;
    }

    public void setDrawBottomLabel(boolean drawBottomLabel) {
        mIsDrawBottomLabel = drawBottomLabel;
    }

    public boolean isDrawTopLabel() {
        return mIsDrawTopLabel;
    }

    public void setDrawTopLabel(boolean drawTopLabel) {
        mIsDrawTopLabel = drawTopLabel;
    }

    public void setSymmetrical(boolean symmetrical) {
        mIsSymmetrical = symmetrical;
    }

    @Override
    public void calc(float min, float max) {
        if (mCusMax || mCusMin || !mIsSymmetrical) {
            super.calc(min, max);
        } else {
            if (max == min) {
                max = min + 1f;
                min = min - 1f;
            }

            if (Math.abs(max) > Math.abs(min)) {
                mMax = max;
                mMin = -max;
            } else {
                mMax = -min;
                mMin = min;
            }
            mRange = mMax - mMin;
        }
    }

    @Override
    public boolean needOffset() {
        return isEnable() && isDrawAxisLabels() && mLabelPosition != null && mLabelPosition.name().endsWith("OUTSIDE");
    }

    public boolean isLabelsInside() {
        return mLabelPosition != null && (mLabelPosition == YAxisLabelPosition.BOTTOM_INSIDE ||
                mLabelPosition == YAxisLabelPosition.CENTER_INSIDE ||
                mLabelPosition == YAxisLabelPosition.TOP_INSIDE);
    }
}
