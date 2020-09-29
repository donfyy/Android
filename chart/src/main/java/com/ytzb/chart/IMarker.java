package com.ytzb.chart;

import android.graphics.Canvas;
import android.util.SparseArray;

import com.ytzb.chart.data.Data;
import com.ytzb.chart.data.Entry;
import com.ytzb.chart.data.Highlight;
import com.ytzb.chart.dataset.IDataSet;

/**
 * Created by xinxin.wang on 18/4/28.
 */

public interface IMarker {
    void draw(Canvas canvas, Highlight highlight, Chart<? extends Data<? extends IDataSet>> chart);

    /**
     * 绑定浮层要显示的数据项
     *
     * @param entries key表示Entry在DataSet中的索引
     * @param data
     */
    void bind(SparseArray<Entry> entries, Data<? extends IDataSet> data);
}
