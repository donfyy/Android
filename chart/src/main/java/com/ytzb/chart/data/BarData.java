package com.ytzb.chart.data;

import com.ytzb.chart.dataset.IBarDataSet;

import java.util.List;

/**
 * Created by xinxin.wang on 18/4/25.
 */

public class BarData extends Data<IBarDataSet> {
    private float mBarWidth = 0.85f;

    public BarData(List<IBarDataSet> dataSets) {
        super(dataSets);
    }

    public float getBarWidth() {
        return mBarWidth;
    }

    public void setBarWidth(float barWidth) {
        mBarWidth = barWidth;
    }
}
