package com.ytzb.chart.dataprovider;

import com.ytzb.chart.data.BarData;

/**
 * Created by xinxin.wang on 18/4/25.
 */

public interface BarProvider extends Provider {
    BarData getBarData();
}
