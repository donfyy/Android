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
 *    Last modified 16-9-28 上午2:20
 *
 */

package com.donfyy.b_viewarchitecture;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.donfyy.conflict.R;
import com.donfyy.b_viewarchitecture.views.Static;


public class TouchEventDemoActivity extends AppCompatActivity {
    private static final String TAG = Static.TAG1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event_demo);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            Log.i(TAG, Static.dispatchTouchEvent);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Log.i(TAG, Static.onTouchEvent);
        }
        return super.onTouchEvent(event);
    }

}
