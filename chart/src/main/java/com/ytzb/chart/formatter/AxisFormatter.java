package com.ytzb.chart.formatter;

import com.ytzb.chart.data.Axis;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by xinxin.wang on 18/5/3.
 */

public class AxisFormatter implements IAxisFormatter {
    protected DecimalFormat mDecimalFormat;

    public AxisFormatter(String pattern) {
        mDecimalFormat = new DecimalFormat(pattern);
        mDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    @Override
    public String getFormattedValue(float value, Axis axis) {
        return mDecimalFormat.format(value);
    }
}
