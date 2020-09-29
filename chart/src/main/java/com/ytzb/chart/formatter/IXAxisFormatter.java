package com.ytzb.chart.formatter;

import android.util.SparseArray;

import com.ytzb.chart.data.Entry;
import com.ytzb.chart.data.XAxis;

/**
 * Created by xinxin.wang on 18/4/24.
 */

public interface IXAxisFormatter extends IAxisFormatter {

    /**
     * 获取指定数据项的在x轴上的显示文本
     *
     * @param xAxis
     * @param entriesMap
     * @return
     */
    String getFormattedValue(XAxis xAxis, SparseArray<Entry> entriesMap);
}
