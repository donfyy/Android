package com.ytzb.chart.data;

import android.graphics.Paint;
import android.graphics.PathEffect;

import com.ytzb.chart.IAxisLabelsComputer;
import com.ytzb.chart.formatter.IAxisFormatter;
import com.ytzb.chart.utils.Utils;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public abstract class Axis implements Cloneable {
    protected float mMin;
    protected float mMax;
    protected float mRange;
    private boolean mEnable;

    public float[] mEntries;
    public String[] mEntryLabels;
    public int mEntryCount = 6;

    private IAxisFormatter mAxisFormatter;

    private float mTextSize;

    private float mXOffset;

    private float mYOffset;

    private float mAxisLineWidth;
    private int mAxisLineColor;
    private boolean mIsDrawAxisLine;

    private float mGridLineWidth;
    private int mGridLineColor;
    private PathEffect mGridLinePathEffect;
    private boolean mIsDrawGridLines;

    protected boolean mCusMin;
    protected boolean mCusMax;
    private int mTextColor;

    private boolean mDrawAxisLabels;

    private boolean mIsAllLabelsInsideBounds;
    /**
     * 高亮的文本的背景色
     */
    private int mHLabelBgColor;
    private float mHLabelXOffset;
    private float mHLabelYOffset;
    private int mHLabelColor;

    private IAxisLabelsComputer mAxisLabelsComputer;

    public void setAxisLabelsComputer(IAxisLabelsComputer axisLabelsComputer) {
        mAxisLabelsComputer = axisLabelsComputer;
    }

    public IAxisLabelsComputer getAxisLabelsComputer() {
        return mAxisLabelsComputer;
    }

    public boolean isDrawAxisLabels() {
        return mDrawAxisLabels;
    }

    public void setDrawAxisLabels(boolean drawAxisLabels) {
        mDrawAxisLabels = drawAxisLabels;
    }

    public boolean isAllLabelsInsideBounds() {
        return mIsAllLabelsInsideBounds;
    }

    public void setAllLabelsInsideBounds(boolean allLabelsInsideBounds) {
        mIsAllLabelsInsideBounds = allLabelsInsideBounds;
    }

    public boolean isEnable() {
        return mEnable;
    }

    public void setEnable(boolean enable) {
        mEnable = enable;
    }

    public float getMin() {
        return mMin;
    }

    public float getMax() {
        return mMax;
    }

    public float getRange() {
        return mRange;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public IAxisFormatter getAxisFormatter() {
        return mAxisFormatter;
    }

    public void setAxisFormatter(IAxisFormatter axisFormatter) {
        mAxisFormatter = axisFormatter;
    }

    public float getXOffset() {
        return mXOffset;
    }

    public void setXOffset(float XOffset) {
        this.mXOffset = XOffset;
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float YOffset) {
        this.mYOffset = YOffset;
    }

    public void setEntryCount(int entryCount) {
        mEntryCount = entryCount;
    }

    public float getAxisLineWidth() {
        return mAxisLineWidth;
    }

    public void setAxisLineWidth(float axisLineWidth) {
        mAxisLineWidth = axisLineWidth;
    }

    public int getAxisLineColor() {
        return mAxisLineColor;
    }

    public void setAxisLineColor(int axisLineColor) {
        mAxisLineColor = axisLineColor;
    }

    public boolean isDrawAxisLine() {
        return mIsDrawAxisLine;
    }

    public void setDrawAxisLine(boolean drawAxisLine) {
        mIsDrawAxisLine = drawAxisLine;
    }

    public float getGridLineWidth() {
        return mGridLineWidth;
    }

    public void setGridLineWidth(float gridLineWidth) {
        mGridLineWidth = gridLineWidth;
    }

    public int getGridLineColor() {
        return mGridLineColor;
    }

    public void setGridLineColor(int gridLineColor) {
        mGridLineColor = gridLineColor;
    }

    public PathEffect getGridLinePathEffect() {
        return mGridLinePathEffect;
    }

    public void setGridLinePathEffect(PathEffect gridLinePathEffect) {
        mGridLinePathEffect = gridLinePathEffect;
    }

    public boolean isDrawGridLines() {
        return mIsDrawGridLines;
    }

    public void setDrawGridLines(boolean drawGridLines) {
        mIsDrawGridLines = drawGridLines;
    }

    public void setMin(float min) {
        mCusMin = true;
        mMin = min;
    }

    public void setMax(float max) {
        mCusMax = true;
        mMax = max;
    }

    public void calc(float min, float max) {
        // TODO: 18/4/26 若没有最大与最小 若最大与最小一致
        mMin = mCusMin ? mMin : min;
        mMax = mCusMax ? mMax : max;

        if (mMax == mMin) {
            mMax = mMin + 1f;
            mMin = mMin - 1f;
        }
        mRange = mMax - mMin;
    }

    public float getRequiredWidth(Paint paint) {
        if (mEntries == null || mEntries.length == 0) {
            return 0;
        }

        if (mEntryLabels == null || mEntryLabels.length == 0) {
            throw new RuntimeException("no labels.");
        }

        paint.setTextSize(mTextSize);

        String longestLabel = getLongestLabel();

        return Utils.calcTextWidth(paint, longestLabel) + getXOffset();
    }

    /**
     * Returns the longest formatted label (in terms of characters), this axis
     * contains.
     *
     * @return
     */
    public String getLongestLabel() {

        String longest = "";

        for (String text : mEntryLabels) {
            if (text != null && longest.length() < text.length())
                longest = text;
        }

        return longest;
    }

    public int getHLabelBgColor() {
        return mHLabelBgColor;
    }

    public void setHLabelBgColor(int HLabelBgColor) {
        mHLabelBgColor = HLabelBgColor;
    }

    public float getHLabelXOffset() {
        return mHLabelXOffset;
    }

    public void setHLabelXOffset(float HLabelXOffset) {
        mHLabelXOffset = HLabelXOffset;
    }

    public float getHLabelYOffset() {
        return mHLabelYOffset;
    }

    public void setHLabelYOffset(float HLabelYOffset) {
        mHLabelYOffset = HLabelYOffset;
    }

    public int getHLabelColor() {
        return mHLabelColor;
    }

    public void setHLabelColor(int HLabelColor) {
        mHLabelColor = HLabelColor;
    }

    public abstract boolean needOffset();

    public float getRequiredHeight(Paint paint) {
        paint.setTextSize(mTextSize);

        String longestLabel = getLongestLabel();
        return Utils.calcTextHeight(paint, longestLabel) + getYOffset();
    }

    public XAxis freeze() {
        try {
            return (XAxis) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void initEntries(int entryCount) {
        if (mEntries == null || mEntries.length != entryCount) {
            mEntries = new float[entryCount];
            mEntryLabels = new String[entryCount];
        }
    }
}
