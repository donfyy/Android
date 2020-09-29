package com.ytzb.chart;

import com.ytzb.chart.data.Data;
import com.ytzb.chart.dataset.IDataSet;

/**
 * Created by xinxin.wang on 18/5/2.
 */

public interface OnChartListener<D extends Data<? extends IDataSet>> {
    void onLoadingMore(Chart<D> chart);
}
