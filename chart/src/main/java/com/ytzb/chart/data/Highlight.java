package com.ytzb.chart.data;

import android.util.SparseArray;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public class Highlight {
    private float mX;
    private float mY;
    private float mXPx;
    private float mYPx;

    private boolean mEnable;

//    private List<Entry> mEntries;

    private SparseArray<Entry> mEntriesMap;

    public boolean isEnable() {
        return mEnable;
    }

    public void setEnable(boolean enable) {
        mEnable = enable;
    }

    public void set(float xPx, float yPx, float x, float y) {
        mXPx = xPx;
        mYPx = yPx;
        mX = x;
        mY = y;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public float getXPx() {
        return mXPx;
    }

    public float getYPx() {
        return mYPx;
    }

//    public List<Entry> getEntries() {
//        return mEntries;
//    }
//
//    public void setEntries(List<Entry> entries) {
//        mEntries = entries;
//    }

    public SparseArray<Entry> getEntriesMap() {
        return mEntriesMap;
    }

    public void setEntriesMap(SparseArray<Entry> entriesMap) {
        mEntriesMap = entriesMap;
    }
}
