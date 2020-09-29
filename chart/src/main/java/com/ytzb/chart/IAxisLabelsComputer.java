package com.ytzb.chart;

import com.ytzb.chart.data.Axis;

/**
 * Created by xinxin.wang on 18/5/3.
 */

public interface IAxisLabelsComputer {
    /**
     * 在给定的闭区间中，计算出要绘制在轴线处的文本
     * @param min
     * @param max
     * @param axis
     */
    void compute(float min, float max, Axis axis);
}
