package com.ytzb.chart.data;

import android.graphics.Color;
import android.util.SparseArray;

import com.google.common.collect.Lists;
import com.ytzb.chart.dataset.IDataSet;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public class Data<D extends IDataSet> {
    public static final Comparator<Entry> ENTRY_COMPARATOR = new Comparator<Entry>() {
        @Override
        public int compare(Entry o1, Entry o2) {
            float delt = o1.getX() - o2.getX();
            if (delt > 0) {
                return 1;
            }

            if (0 > delt) {
                return -1;
            }
            return 0;
        }
    };
    private List<D> mDataSets;

    private float mMinY;
    private float mMaxY;

    private float mMinX;
    private float mMaxX;

    /**
     * 用于在视窗中为x轴两边留一些空白，使得曲线中某一点能与条形图的对应矩形中心的点在一条垂直直线上
     */
    private float mExtraMinXOffset;
    private float mExtraMaxXOffset;

    private float mMinRY;
    private float mMaxRY;
    private float mMinLY;
    private float mMaxLY;

    private float mHighlightLineWidth = 3;
    private int mHighlightColor = Color.YELLOW;

    public Data(List<D> dataSets) {
        mDataSets = dataSets;

        notifyDataSetChanged();
    }

    public void calcMinMaxY(float fromX, float toX) {
        for (final IDataSet dataSet : mDataSets) {
            dataSet.calcMinMaxY(fromX, toX);
        }

        calcMinMaxY();
    }

    public void notifyDataSetChanged() {
        calcMinMax();
    }

    public void calcMinMax() {
        calcMinMaxX();
        calcMinMaxY();
    }

    private void calcMinMaxX() {
        mMinX = Float.MAX_VALUE;
        mMaxX = -Float.MAX_VALUE;

        for (final IDataSet dataSet : mDataSets) {
            calcMinMaxX(dataSet);
        }
    }

    private void calcMinMaxY() {
        mMinLY = Float.MAX_VALUE;
        mMaxLY = -Float.MAX_VALUE;
        mMinRY = Float.MAX_VALUE;
        mMaxRY = -Float.MAX_VALUE;
        mMinY = Float.MAX_VALUE;
        mMaxY = -Float.MAX_VALUE;

        for (final D dataSet : mDataSets) {
            calcMinMaxY(dataSet);
        }
    }

    private void calcMinMax(IDataSet dataSet) {
        calcMinMaxX(dataSet);
        calcMinMaxY(dataSet);
    }

    private void calcMinMaxX(IDataSet dataSet) {
        if (dataSet.getMinX() < mMinX) {
            mMinX = dataSet.getMinX();
        }

        if (dataSet.getMaxX() > mMaxX) {
            mMaxX = dataSet.getMaxX();
        }
    }

    private void calcMinMaxY(IDataSet dataSet) {
        if (dataSet.getMinY() < mMinY) {
            mMinY = dataSet.getMinY();
        }

        if (dataSet.getMaxY() > mMaxY) {
            mMaxY = dataSet.getMaxY();
        }

        if (dataSet.getAxisDependency() == AxisDependency.LEFT) {
            if (dataSet.getMinY() < mMinLY) {
                mMinLY = dataSet.getMinY();
            }

            if (dataSet.getMaxY() > mMaxLY) {
                mMaxLY = dataSet.getMaxY();
            }
        }

        if (dataSet.getAxisDependency() == AxisDependency.RIGHT) {
            if (dataSet.getMinY() < mMinRY) {
                mMinRY = dataSet.getMinY();
            }

            if (dataSet.getMaxY() > mMaxRY) {
                mMaxRY = dataSet.getMaxY();
            }
        }
    }

    public float getMinY() {
        return mMinY;
    }

    public float getMaxY() {
        return mMaxY;
    }

    public float getExtraMinXOffset() {
        return mExtraMinXOffset;
    }

    public void setExtraMinXOffset(float extraMinXOffset) {
        mExtraMinXOffset = extraMinXOffset;
    }

    public float getExtraMaxXOffset() {
        return mExtraMaxXOffset;
    }

    public void setExtraMaxXOffset(float extraMaxXOffset) {
        mExtraMaxXOffset = extraMaxXOffset;
    }

    public float getMinY(AxisDependency axisDependency) {
        if (axisDependency == AxisDependency.LEFT) {
            return mMinLY;
        }

        return mMinRY;
    }

    public float getMaxY(AxisDependency axisDependency) {
        if (axisDependency == AxisDependency.LEFT) {
            return mMaxLY;
        }

        return mMaxRY;
    }

    public float getMinX() {
        return mMinX;
    }

    public float getMaxX() {
        return mMaxX;
    }

    public float getMaxXWithOffset() {
        return mMaxX + mExtraMaxXOffset;
    }

    public float getMinXWithOffset() {
        return mMinX - mExtraMinXOffset;
    }

    public List<D> getDataSets() {
        return mDataSets;
    }

    public D getDataSet(int index) {
        return mDataSets.get(index);
    }

    /**
     * Returns the first DataSet from the datasets-array that has it's dependency on the left axis.
     * Returns null if no DataSet with left dependency could be found.
     *
     * @return
     */
    protected D getFirstLeft(List<D> sets) {
        for (D dataSet : sets) {
            if (dataSet.getAxisDependency() == AxisDependency.LEFT)
                return dataSet;
        }
        return null;
    }

    /**
     * Returns the first DataSet from the datasets-array that has it's dependency on the right axis.
     * Returns null if no DataSet with right dependency could be found.
     *
     * @return
     */
    public D getFirstRight(List<D> sets) {
        for (D dataSet : sets) {
            if (dataSet.getAxisDependency() == AxisDependency.RIGHT)
                return dataSet;
        }
        return null;
    }

    public float getHighlightLineWidth() {
        return mHighlightLineWidth;
    }

    public void setHighlightLineWidth(float highlightLineWidth) {
        mHighlightLineWidth = highlightLineWidth;
    }

    public int getHighlightColor() {
        return mHighlightColor;
    }

    public void setHighlightColor(int highlightColor) {
        mHighlightColor = highlightColor;
    }

    private List<Entry> mEntriesBuffer = Lists.newArrayList();

    /**
     * 找到每一个数据集中最接近指定xy的数据项，再从这些数据项集合里找到最接近x的若干数据项,
     * 此处未区分数据项是基于左坐标系还是右坐标系
     *
     * @param x
     * @param y
     * @param rounding
     * @return
     */
    public List<Entry> getEntryForXValue(float x, float y, DataSet.Rounding rounding) {
        mEntriesBuffer.clear();
        for (final D dataSet : mDataSets) {
            Entry entry = dataSet.getEntryForXValue(x, y, rounding);
            if (entry != null) {
                mEntriesBuffer.add(entry);
            }
        }

        if (mEntriesBuffer.isEmpty()) {
            return Collections.emptyList();
        }

        Collections.sort(mEntriesBuffer, ENTRY_COMPARATOR);


        int i = DataSet.getClosestEntryIndex(x, Float.NaN, rounding, mEntriesBuffer);
        if (i != -1) {
            List<Entry> ret = Lists.newArrayList();
            Entry closestEntry = mEntriesBuffer.get(i);
            for (final Entry entry : mEntriesBuffer) {
                if (entry.getX() == closestEntry.getX()) {
                    ret.add(entry);
                }
            }

            return ret;
        }
        return Collections.emptyList();
    }

    private SparseArray<Entry> mEntrySparseArray = new SparseArray<>();

    /**
     * 找到每一个数据集中最接近指定xy的数据项，再从这些数据项集合里找到最接近x的若干数据项,
     * 此处未区分数据项是基于左坐标系还是右坐标系
     *
     * @param x
     * @param y
     * @param rounding
     * @param outMap   key 表示Entry所在的DataSet索引
     */
    public void getEntriesForXValue(float x, float y, DataSet.Rounding rounding, SparseArray<Entry> outMap) {
        mEntriesBuffer.clear();
        mEntrySparseArray.clear();
        outMap.clear();
        for (int i = 0; i < mDataSets.size(); i++) {
            D dataSet = mDataSets.get(i);
            Entry entry = dataSet.getEntryForXValue(x, y, rounding);
            if (entry != null) {
                mEntriesBuffer.add(entry);
                mEntrySparseArray.append(i, entry);
            }
        }

        if (mEntriesBuffer.isEmpty()) {
            return;
        }

        Collections.sort(mEntriesBuffer, ENTRY_COMPARATOR);


        int i = DataSet.getClosestEntryIndex(x, Float.NaN, rounding, mEntriesBuffer);
        if (i != -1) {
            Entry closestEntry = mEntriesBuffer.get(i);
            for (final Entry entry : mEntriesBuffer) {
                if (entry.getX() == closestEntry.getX()) {
                    int indexOfValue = mEntrySparseArray.indexOfValue(entry);
                    outMap.append(mEntrySparseArray.keyAt(indexOfValue), entry);
                }
            }
        }
    }
}
