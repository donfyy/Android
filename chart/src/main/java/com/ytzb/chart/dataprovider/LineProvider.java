package com.ytzb.chart.dataprovider;

import com.ytzb.chart.data.LineData;

/**
 * Created by xinxin.wang on 18/4/25.
 */

public interface LineProvider extends Provider {
    LineData getLineData();
}
