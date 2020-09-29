package com.ytzb.chart.renderer;


import android.graphics.Canvas;
import android.graphics.Paint;

import com.ytzb.chart.Viewport;
import com.ytzb.chart.data.Data;
import com.ytzb.chart.data.Highlight;
import com.ytzb.chart.dataprovider.Provider;
import com.ytzb.chart.dataset.IDataSet;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public abstract class DataRenderer<P extends Provider> extends Renderer {
    protected P mChart;

    protected Paint mRenderPaint;

    protected Paint mHighlightPaint;

    protected XBounds mXBounds = new XBounds();

    public DataRenderer(Viewport viewport, P chart) {
        super(viewport);
        mChart = chart;

        mRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public abstract void renderData(Canvas canvas);

    public void renderHighlight(Canvas canvas, Highlight highlight) {
        Data<? extends IDataSet> data = mChart.getData();
        mHighlightPaint.setColor(data.getHighlightColor());
        mHighlightPaint.setStrokeWidth(data.getHighlightLineWidth());

        if (mViewport.isXInBounds(highlight.getXPx())) {
            canvas.drawLine(highlight.getXPx(),
                    mViewport.offsetTop(),
                    highlight.getXPx(),
                    mViewport.contentHeight() + mViewport.offsetTop(),
                    mHighlightPaint);
        }

        if (mViewport.isYInBounds(highlight.getYPx())) {
            canvas.drawLine(mViewport.offsetLeft(),
                    highlight.getYPx(),
                    mViewport.offsetLeft() + mViewport.contentWidth(),
                    highlight.getYPx(),
                    mHighlightPaint);
        }
    }
}
