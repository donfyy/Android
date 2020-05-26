package com.xiangxue.willremove.intercept.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class MyUtils {

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
