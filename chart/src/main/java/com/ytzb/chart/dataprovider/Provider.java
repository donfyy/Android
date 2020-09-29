package com.ytzb.chart.dataprovider;

import com.ytzb.chart.Transformer;
import com.ytzb.chart.data.AxisDependency;
import com.ytzb.chart.data.Data;
import com.ytzb.chart.data.XAxis;
import com.ytzb.chart.dataset.IDataSet;

/**
 * Created by xinxin.wang on 18/4/25.
 */

public interface Provider {
    Transformer getTransformer(AxisDependency axisDependency);

    XAxis getXAxis();

    Data<? extends IDataSet> getData();
}
