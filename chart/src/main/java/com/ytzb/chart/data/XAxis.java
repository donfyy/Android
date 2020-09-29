package com.ytzb.chart.data;


import com.ytzb.chart.formatter.IXAxisFormatter;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public class XAxis extends Axis implements Cloneable {

    private XAxisLabelPosition mLabelPosition = XAxisLabelPosition.CENTER_OUTSIDE;
    /**
     * 与x轴垂直直线距边框的距离大于此距离时，方可绘制出来，用来微调
     * */
    private float mGridLineOffset = 100;

    public void setLabelPosition(XAxisLabelPosition labelPosition) {
        mLabelPosition = labelPosition;
    }

    public XAxisLabelPosition getLabelPosition() {
        return mLabelPosition;
    }

    public IXAxisFormatter getXAxisFormatter() {
        return ((IXAxisFormatter) getAxisFormatter());
    }

    @Override
    public void calc(float min, float max) {
        super.calc(min, max);
    }

    @Override
    public boolean needOffset() {
        return isEnable() && isDrawAxisLabels() && (mLabelPosition == XAxisLabelPosition.CENTER_OUTSIDE ||
                mLabelPosition == XAxisLabelPosition.LEFT_OUTSIDE ||
                mLabelPosition == XAxisLabelPosition.RIGHT_OUTSIDE);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public float getGridLineOffset() {
        return mGridLineOffset;
    }

    public void setGridLineOffset(float gridLineOffset) {
        mGridLineOffset = gridLineOffset;
    }
}
