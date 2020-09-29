package com.ytzb.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.ytzb.chart.data.LineData;
import com.ytzb.chart.dataprovider.LineProvider;
import com.ytzb.chart.renderer.LineRenderer;

/**
 * Created by xinxin.wang on 18/4/26.
 */

public class LineChart extends Chart<LineData> implements LineProvider {
    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();

        mDataRenderer = new LineRenderer(mViewport, this);
    }

    @Override
    public LineData getLineData() {
        return mData;
    }
}
