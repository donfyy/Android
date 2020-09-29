package com.ytzb.chart.data;

import com.ytzb.chart.dataset.ILineDataSet;

import java.util.List;

/**
 * Created by xinxin.wang on 18/4/25.
 */

public class LineData extends Data<ILineDataSet> {
    public LineData(List<ILineDataSet> dataSets) {
        super(dataSets);
    }
}
