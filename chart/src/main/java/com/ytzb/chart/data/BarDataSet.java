package com.ytzb.chart.data;

import com.ytzb.chart.dataset.IBarDataSet;

import java.util.List;

/**
 * Created by xinxin.wang on 18/5/2.
 */

public class BarDataSet extends DataSet implements IBarDataSet {
    private int mPositiveColor;
    private int mNegativeColor;

    public BarDataSet(List<Entry> values) {
        super(values);
    }

    @Override
    public int getPositiveColor() {
        return mPositiveColor;
    }

    @Override
    public int getNegativeColor() {
        return mNegativeColor;
    }

    public void setPositiveColor(int positiveColor) {
        mPositiveColor = positiveColor;
    }

    public void setNegativeColor(int negativeColor) {
        mNegativeColor = negativeColor;
    }
}
