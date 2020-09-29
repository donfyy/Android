package com.ytzb.chart.data;

import com.ytzb.chart.dataset.ILineDataSet;

import java.util.List;

/**
 * Created by xinxin.wang on 18/4/25.
 */

public class LineDataSet extends DataSet implements ILineDataSet {
    private float mLineWidth = 1;

    public LineDataSet(List<Entry> values) {
        super(values);
    }

    @Override
    public float getLineWidth() {
        return mLineWidth;
    }

    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
    }
}
