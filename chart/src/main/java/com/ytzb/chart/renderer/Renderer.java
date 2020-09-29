package com.ytzb.chart.renderer;


import com.ytzb.chart.Viewport;

/**
 * Created by xinxin.wang on 18/4/23.
 */

public abstract class Renderer {
    protected Viewport mViewport;

    public Renderer(Viewport viewport) {
        mViewport = viewport;
    }
}
