package com.ytzb.chart.data;

import android.graphics.Color;

import com.google.common.collect.Lists;
import com.ytzb.chart.dataset.IDataSet;

import java.util.List;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public class DataSet implements IDataSet {
    private List<Entry> mValues;

    private float mMaxY;
    private float mMinY;
    private float mMinX;
    private float mMaxX;

    private AxisDependency mAxisDependency = AxisDependency.LEFT;

    private int mColor = Color.RED;

    public DataSet(List<Entry> values) {
        mValues = values;

        if (mValues == null) {
            mValues = Lists.newArrayList();
        }

        calcMinMax();
    }

    @Override
    public void calcMinMaxY(float fromX, float toX) {
        int from = getEntryIndex(fromX, Float.NaN, Rounding.UP);
        int to = getEntryIndex(toX, Float.NaN, Rounding.DOWN);

        mMaxY = -Float.MAX_VALUE;
        mMinY = Float.MAX_VALUE;


        if (from == -1) {
            return;
        }

        if (mValues.get(from).getX() > toX) {
            return;
        }

        if (mValues.get(to).getX() < fromX) {
            return;
        }

        for (int i = from; i <= to; i++) {
            Entry entry = mValues.get(i);
            calcMinMaxY(entry);
        }
    }

    private void calcMinMaxY(Entry entry) {
        if (entry.getY() > mMaxY) {
            mMaxY = entry.getY();
        }

        if (mMinY > entry.getY()) {
            mMinY = entry.getY();
        }
    }

    @Override
    public boolean addEntry(Entry entry) {
        if (entry == null) {
            return false;
        }

        if (mValues == null) {
            mValues = Lists.newArrayList();
        }

        if (mValues.add(entry)) {
            calcMinMax(entry);

            return true;
        }

        return false;
    }

    @Override
    public boolean removeEntry(Entry entry) {
        if (entry != null) {
            return false;
        }

        if (mValues == null) {
            return false;
        }

        if (mValues.remove(entry)) {
            calcMinMax();

            return true;
        }

        return false;
    }

    @Override
    public void addEntries(List<Entry> entries) {
        mValues.addAll(entries);

        calcMinMax();
    }

    @Override
    public void addEntries(int index, List<Entry> entries) {
        mValues.addAll(index, entries);

        calcMinMax();
    }

    private void calcMinMax(Entry entry) {
        if (entry == null) {
            return;
        }

        calcMinMaxX(entry);
        calcMinMaxY(entry);
    }

    @Override
    public Entry getEntryForIndex(int i) {
        return mValues.get(i);
    }

    @Override
    public float getMaxY() {
        return mMaxY;
    }

    @Override
    public float getMinY() {
        return mMinY;
    }

    @Override
    public int getColor() {
        return mColor;
    }

    @Override
    public int indexOf(Entry entry) {
        if (mValues != null) {
            return mValues.indexOf(entry);
        }

        return -1;
    }

    public void setColor(int color) {
        mColor = color;
    }

    @Override
    public float getMaxX() {
        return mMaxX;
    }

    @Override
    public float getMinX() {
        return mMinX;
    }

    @Override
    public AxisDependency getAxisDependency() {
        return mAxisDependency;
    }

    @Override
    public void setAxisDependency(AxisDependency axisDependency) {
        mAxisDependency = axisDependency;
    }

    @Override
    public Entry getEntryForXValue(float xValue, float closestToY, Rounding rounding) {

        int index = getEntryIndex(xValue, closestToY, rounding);
        if (index > -1)
            return mValues.get(index);
        return null;
    }

    @Override
    public int getEntryIndex(float xValue, float closestToY, Rounding rounding) {
        return getClosestEntryIndex(xValue, closestToY, rounding, mValues);
    }

    public static int getClosestEntryIndex(float xValue, float closestToY, Rounding rounding, List<Entry> values) {
        if (values == null || values.isEmpty())
            return -1;

        int low = 0;
        int high = values.size() - 1;
        int closest = high;

        while (low < high) {
            int m = (low + high) / 2;

            final float d1 = values.get(m).getX() - xValue,
                    d2 = values.get(m + 1).getX() - xValue,
                    ad1 = Math.abs(d1), ad2 = Math.abs(d2);

            if (ad2 < ad1) {
                // [m + 1] is closer to xValue
                // Search in an higher place
                low = m + 1;
            } else if (ad1 < ad2) {
                // [m] is closer to xValue
                // Search in a lower place
                high = m;
            } else {
                // We have multiple sequential x-value with same distance

                if (d1 >= 0.0) {
                    // Search in a lower place
                    high = m;
                } else if (d1 < 0.0) {
                    // Search in an higher place
                    low = m + 1;
                }
            }

            closest = high;
        }

        if (closest != -1) {
            float closestXValue = values.get(closest).getX();
            if (rounding == Rounding.UP) {
                // If rounding up, and found x-value is lower than specified x, and we can go upper...
                if (closestXValue < xValue && closest < values.size() - 1) {
                    ++closest;
                }
            } else if (rounding == Rounding.DOWN) {
                // If rounding down, and found x-value is upper than specified x, and we can go lower...
                if (closestXValue > xValue && closest > 0) {
                    --closest;
                }
            }

            // Search by closest to y-value
            if (!Float.isNaN(closestToY)) {
                while (closest > 0 && values.get(closest - 1).getX() == closestXValue)
                    closest -= 1;

                float closestYValue = values.get(closest).getY();
                int closestYIndex = closest;

                while (true) {
                    closest += 1;
                    if (closest >= values.size())
                        break;

                    final Entry value = values.get(closest);

                    if (value.getX() != closestXValue)
                        break;

                    if (Math.abs(value.getY() - closestToY) < Math.abs(closestYValue - closestToY)) {
                        closestYValue = closestToY;
                        closestYIndex = closest;
                    }
                }

                closest = closestYIndex;
            }
        }

        return closest;
    }

    @Override
    public void calcMinMax() {
        if (mValues == null || mValues.isEmpty()) {
            return;
        }

        mMaxY = -Float.MAX_VALUE;
        mMinY = Float.MAX_VALUE;

        mMaxX = -Float.MAX_VALUE;
        mMinX = Float.MAX_VALUE;

        for (final Entry value : mValues) {
            calcMinMaxX(value);

            calcMinMaxY(value);
        }
    }

    private void calcMinMaxX(Entry value) {
        if (value.getX() > mMaxX) {
            mMaxX = value.getX();
        }

        if (mMinX > value.getX()) {
            mMinX = value.getX();
        }
    }

    /**
     * Determines how to round DataSet index values for
     * {@link #getEntryIndex(float, float, Rounding)} DataSet.getEntryIndex()}
     * when an exact x-index is not found.
     */
    public enum Rounding {
        UP,
        DOWN,
        CLOSEST,
    }
}
