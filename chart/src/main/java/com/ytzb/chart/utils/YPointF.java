package com.ytzb.chart.utils;

import com.github.mikephil.charting.utils.ObjectPool;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public class YPointF extends ObjectPool.Poolable {
    private static ObjectPool<YPointF> pool;

    public float x;
    public float y;

    static {
        pool = ObjectPool.create(32, new YPointF(0, 0));
        pool.setReplenishPercentage(0.5f);
    }

    public YPointF() {
    }

    public YPointF(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static YPointF getInstance(float x, float y) {
        YPointF result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    public static void recycleInstance(YPointF instance) {
        pool.recycle(instance);
    }

    @Override
    protected ObjectPool.Poolable instantiate() {
        return new YPointF(0, 0);
    }
}
