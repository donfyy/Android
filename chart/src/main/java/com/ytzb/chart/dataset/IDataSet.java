package com.ytzb.chart.dataset;

import com.ytzb.chart.data.AxisDependency;
import com.ytzb.chart.data.DataSet;
import com.ytzb.chart.data.Entry;

import java.util.List;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public interface IDataSet {
    void calcMinMaxY(float fromX, float toX);

    boolean addEntry(Entry entry);

    boolean removeEntry(Entry entry);

    void addEntries(List<Entry> entries);

    void addEntries(int index, List<Entry> entries);

    Entry getEntryForIndex(int i);

    float getMaxX();

    float getMinX();

    AxisDependency getAxisDependency();

    void setAxisDependency(AxisDependency axisDependency);

    /**
     * Returns the first Entry object found at the given x-value with binary
     * search.
     * If the no Entry at the specified x-value is found, this method
     * returns the Entry at the closest x-value according to the rounding.
     * INFORMATION: This method does calculations at runtime. Do
     * not over-use in performance critical situations.
     *
     * @param xValue     the x-value
     * @param closestToY If there are multiple y-values for the specified x-value,
     * @param rounding   determine whether to round up/down/closest
     * @return -1 if there is no Entry matching the provided x-value
     */
    Entry getEntryForXValue(float xValue, float closestToY, DataSet.Rounding rounding);

    /**
     * Returns the first Entry index found at the given x-value with binary
     * search.
     * If the no Entry at the specified x-value is found, this method
     * returns the Entry at the closest x-value according to the rounding.
     * INFORMATION: This method does calculations at runtime. Do
     * not over-use in performance critical situations.
     *
     * @param xValue     the x-value
     * @param closestToY If there are multiple y-values for the specified x-value,
     * @param rounding   determine whether to round up/down/closest
     * @return -1 if there is no Entry matching the provided x-value
     */
    int getEntryIndex(float xValue, float closestToY, DataSet.Rounding rounding);

    void calcMinMax();

    float getMaxY();

    float getMinY();

    int getColor();

    int indexOf(Entry entry);
}
