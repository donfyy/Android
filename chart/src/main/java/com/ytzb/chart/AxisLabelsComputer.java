package com.ytzb.chart;

import com.ytzb.chart.data.Axis;

/**
 * Created by xinxin.wang on 18/5/4.
 */

public class AxisLabelsComputer implements IAxisLabelsComputer {
    @Override
    public void compute(float min, float max, Axis mAxis) {
        float interval = (max - min) / (mAxis.mEntryCount - 1);

        if (interval <= 0) {
            mAxis.mEntries = null;
            return;
        }

        mAxis.initEntries(mAxis.mEntryCount);

        for (int i = 0; i < mAxis.mEntryCount; i++) {
            float v = min + i * interval;
            mAxis.mEntries[i] = v;
            mAxis.mEntryLabels[i] = mAxis.getAxisFormatter().getFormattedValue(v, mAxis);
        }
    }
}
