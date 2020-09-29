package com.ytzb.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.ytzb.chart.data.BarData;
import com.ytzb.chart.dataprovider.BarProvider;
import com.ytzb.chart.renderer.BarRenderer;

/**
 * Created by xinxin.wang on 18/4/26.
 */

public class BarChart extends Chart<BarData> implements BarProvider {
    public BarChart(Context context) {
        super(context);
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();

        mDataRenderer = new BarRenderer(mViewport, this);
    }

    @Override
    public BarData getBarData() {
        return mData;
    }
}
