package com.ytzb.chart.formatter;

import android.util.SparseArray;

import com.ytzb.chart.data.Entry;
import com.ytzb.chart.data.XAxis;

/**
 * Created by xinxin.wang on 18/4/26.
 */

public class XAxisFormatter extends AxisFormatter implements IXAxisFormatter {

    public XAxisFormatter(String pattern) {
        super(pattern);
    }

    @Override
    public String getFormattedValue(XAxis xAxis, SparseArray<Entry> entriesMap) {
        return mDecimalFormat.format(entriesMap.valueAt(0).getX());
    }
}
