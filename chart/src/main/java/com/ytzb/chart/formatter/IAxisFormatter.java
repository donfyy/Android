package com.ytzb.chart.formatter;

import com.ytzb.chart.data.Axis;

/**
 * Created by xinxin.wang on 18/4/24.
 */

public interface IAxisFormatter {
    String getFormattedValue(float value, Axis axis);
}
