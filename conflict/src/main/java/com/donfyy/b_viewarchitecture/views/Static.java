/*
 * Copyright 2016 GcsSloop
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    Last modified 16-9-28 上午1:25
 *
 */

package com.donfyy.b_viewarchitecture.views;

import android.util.Log;
import android.view.MotionEvent;

public class Static {
    public static String TAG1 = "MainActivity []";
    public static String TAG2 = "RootView     []";
    public static String TAG3 = "ViewGroupA   []";
    public static String TAG4 = "View1        []";
    public static String TAG5 = "View2        []";

    public static String dispatchTouchEvent    = "dispatchTouchEvent     []";
    public static String onInterceptTouchEvent = "onInterceptTouchEvent  []";
    public static String onTouchEvent          = "onTouchEvent           []";

    public static void printMotionEventAction(String tag, String method, MotionEvent event){

        int action = event.getActionMasked();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                Log.e(tag,  event.getPointerCount()+String.format("%30s",method)  + ":MotionEvent.ACTION_DOWN           x:y=" + (int)event.getX() + ":" + (int)event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e(tag, event.getPointerCount()+String.format("%30s",method)   + ":MotionEvent.ACTION_MOVE           x:y=" + (int)event.getX() + ":" + (int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                Log.e(tag, event.getPointerCount()+String.format("%30s",method)   + ":MotionEvent.ACTION_UP             x:y=" + (int)event.getX() + ":" + (int)event.getY());
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(tag, event.getPointerCount()+String.format("%30s",method)   + ":MotionEvent.ACTION_CANCEL         x:y=" + (int)event.getX() + ":" + (int)event.getY());
                break;
            case MotionEvent.ACTION_OUTSIDE:
                Log.e(tag, event.getPointerCount()+String.format("%30s",method)   + ":MotionEvent.ACTION_OUTSIDE        x:y=" + (int)event.getX() + ":" + (int)event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e(tag, event.getPointerCount()+String.format("%30s",method)   + ":MotionEvent.ACTION_POINTER_DOWN   x:y=" + (int)event.getX() + ":" + (int)event.getY());
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                Log.e(tag, event.getPointerCount()+String.format("%30s",method)   + ":MotionEvent.ACTION_HOVER_MOVE     x:y=" + (int)event.getX() + ":" + (int)event.getY());
                break;
        }
    }
}

