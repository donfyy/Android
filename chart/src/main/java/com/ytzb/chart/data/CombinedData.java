package com.ytzb.chart.data;

import java.util.List;

/**
 * Created by xinxin.wang on 18/4/26.
 */

public class CombinedData extends Data<DataSet> {
    private LineData mLineData;
    private BarData mBarData;

    public CombinedData(List<DataSet> dataSets) {
        super(dataSets);
    }
}
