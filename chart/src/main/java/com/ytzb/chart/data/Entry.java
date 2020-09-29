package com.ytzb.chart.data;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public class Entry {
    private float mX;
    private float mY;
    private Object mData;

    public Entry(float x, float y) {
        mX = x;
        mY = y;
    }

    public Entry(float x, float y, Object data) {
        mX = x;
        mY = y;
        mData = data;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        mY = y;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object data) {
        mData = data;
    }
}
