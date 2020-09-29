package com.ytzb.chart.renderer;

import com.ytzb.chart.data.DataSet;
import com.ytzb.chart.data.XAxis;
import com.ytzb.chart.dataprovider.Provider;
import com.ytzb.chart.dataset.IDataSet;

/**
 * Class representing the bounds of the current viewport in terms of indices in the values array of a DataSet.
 */

public class XBounds {
    /**
     * minimum visible entry index
     */
    public int min;

    /**
     * maximum visible entry index
     */
    public int max;

    /**
     * range of visible entry indices
     */
    public int range;

    /**
     * Calculates the minimum and maximum x values as well as the range between them.
     *
     * @param chart
     * @param dataSet
     */
    public void set(Provider chart, IDataSet dataSet) {
        XAxis xAxis = chart.getXAxis();

        int fromIdx = dataSet.getEntryIndex(xAxis.getMin(), Float.NaN, DataSet.Rounding.UP);

        if (fromIdx == -1 || dataSet.getEntryForIndex(fromIdx).getX() > xAxis.getMax()) {
            reset();
            return;
        }

        int toIdx = dataSet.getEntryIndex(xAxis.getMax(), Float.NaN, DataSet.Rounding.DOWN);

        if (toIdx == -1 || dataSet.getEntryForIndex(toIdx).getX() < xAxis.getMin()) {
            reset();
            return;
        }

        min = fromIdx;
        max = toIdx;
        range = max - min;
    }

    private void reset() {
        min = 0;
        max = 0;
        range = -1;
    }
}
