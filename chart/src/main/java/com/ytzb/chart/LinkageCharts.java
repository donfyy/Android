package com.ytzb.chart;

import com.ytzb.chart.data.Data;
import com.ytzb.chart.dataset.IDataSet;

import java.util.List;

/**
 * Created by xinxin.wang on 18/5/3.
 */

public class LinkageCharts {

    private List<Chart<? extends Data<? extends IDataSet>>> mCharts;

    /**
     * @param charts 图表在屏幕上从上到下顺序显示，无间断
     */
    public LinkageCharts(List<Chart<? extends Data<? extends IDataSet>>> charts) {
        mCharts = charts;
    }

    /**
     * 根据inChart的y坐标，获取相对于outChart的y坐标
     *
     * @param yPx
     * @param outChart
     * @param inChart
     * @return
     */
    public float getHighlightY(float yPx, Chart outChart, Chart inChart) {
        int inIdx = mCharts.indexOf(inChart);

        int outIdx = mCharts.indexOf(outChart);

        float yOfOutChartXAxis = 0;

        if (outIdx > inIdx) {
            for (int i = inIdx; i < outIdx; i++) {
                yOfOutChartXAxis += mCharts.get(i).getHeight();
            }
        } else {
            for (int i = outIdx; i < inIdx; i++) {
                yOfOutChartXAxis -= mCharts.get(i).getHeight();
            }
        }

        return yPx - yOfOutChartXAxis;
    }
}
