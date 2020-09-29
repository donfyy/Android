package com.ytzb.chart.renderer;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.ytzb.chart.Transformer;
import com.ytzb.chart.Viewport;
import com.ytzb.chart.data.BarData;
import com.ytzb.chart.data.Entry;
import com.ytzb.chart.dataprovider.BarProvider;
import com.ytzb.chart.dataset.IBarDataSet;

/**
 * Created by xinxin.wang on 18/4/25.
 */

public class BarRenderer extends DataRenderer<BarProvider> {
    private RectF mBuffer = new RectF();

    public BarRenderer(Viewport viewport, BarProvider chart) {
        super(viewport, chart);
    }

    @Override
    public void renderData(Canvas canvas) {
        BarData barData = mChart.getBarData();

        for (final IBarDataSet dataSet : barData.getDataSets()) {
            renderBar(dataSet, canvas);
        }
    }

    private void renderBar(IBarDataSet dataSet, Canvas canvas) {
        mXBounds.set(mChart, dataSet);

        Transformer transformer = mChart.getTransformer(dataSet.getAxisDependency());

        float barWidth = mChart.getBarData().getBarWidth();
        float barHalfWidth = barWidth / 2f;

        for (int i = mXBounds.min; i <= mXBounds.max; i++) {
            Entry entry = dataSet.getEntryForIndex(i);

            mBuffer.left = entry.getX() - barHalfWidth;
            mBuffer.right = entry.getX() + barHalfWidth;

            if (entry.getY() > 0) {
                mRenderPaint.setColor(dataSet.getPositiveColor());
                mBuffer.top = entry.getY();
                mBuffer.bottom = 0;
            } else if (entry.getY() < 0) {
                mRenderPaint.setColor(dataSet.getNegativeColor());
                mBuffer.top = 0;
                mBuffer.bottom = entry.getY();
            } else {
                continue;
            }

            transformer.pointValuesToPixel(mBuffer);

            canvas.drawRect(mBuffer, mRenderPaint);
        }
    }
}
