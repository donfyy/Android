package com.ytzb.chart.renderer;

import android.graphics.Canvas;

import com.ytzb.chart.Transformer;
import com.ytzb.chart.Viewport;
import com.ytzb.chart.data.Entry;
import com.ytzb.chart.data.LineData;
import com.ytzb.chart.dataprovider.LineProvider;
import com.ytzb.chart.dataset.ILineDataSet;

/**
 * Created by xinxin.wang on 18/4/25.
 */

public class LineRenderer extends DataRenderer<LineProvider> {
    public LineRenderer(Viewport viewport, LineProvider chart) {
        super(viewport, chart);
    }

    @Override
    public void renderData(Canvas canvas) {
        LineData lineData = mChart.getLineData();

        for (final ILineDataSet dataSet : lineData.getDataSets()) {
            renderLine(canvas, dataSet);
        }
    }

    private float[] mBuffer = new float[4];

    private void renderLine(Canvas canvas, ILineDataSet dataSet) {
        mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
        mRenderPaint.setColor(dataSet.getColor());

        mXBounds.set(mChart, dataSet);

        if (mXBounds.range == -1) {
            return;
        }

        Transformer transformer = mChart.getTransformer(dataSet.getAxisDependency());
        if (mXBounds.range == 0) {
            Entry entry = dataSet.getEntryForIndex(mXBounds.min);
            mBuffer[0] = entry.getX();
            mBuffer[1] = entry.getY();

            transformer.pointValuesToPixel(mBuffer);

            canvas.drawPoint(mBuffer[0], mBuffer[1], mRenderPaint);
            return;
        }

        for (int i = mXBounds.min; i < mXBounds.max; i++) {
            Entry curr = dataSet.getEntryForIndex(i);
            Entry next = dataSet.getEntryForIndex(i + 1);

            mBuffer[0] = curr.getX();
            mBuffer[1] = curr.getY();
            mBuffer[2] = next.getX();
            mBuffer[3] = next.getY();

            transformer.pointValuesToPixel(mBuffer);
            canvas.drawLine(mBuffer[0], mBuffer[1], mBuffer[2], mBuffer[3], mRenderPaint);
        }
    }
}
