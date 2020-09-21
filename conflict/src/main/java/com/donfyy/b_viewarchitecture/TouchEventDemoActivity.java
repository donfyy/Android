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

import com.blankj.utilcode.util.LogUtils;
import com.donfyy.b_viewarchitecture.views.Static;
import com.donfyy.conflict.R;
import com.donfyy.conflict.databinding.ActivityTouchEventDemoBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


public class TouchEventDemoActivity extends AppCompatActivity {
    private static final String TAG = Static.TAG1;
    private ActivityTouchEventDemoBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_touch_event_demo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.ll1.postDelayed(new Runnable() {
            @Override
            public void run() {
                test();
            }
        }, 2000);
    }

    private void test() {
        int[] location = new int[2];
        mBinding.ll1.getLocationInWindow(location);
        LogUtils.d(location[0], location[1]);
        mBinding.rootView1.getLocationInWindow(location);
        LogUtils.d(location[0], location[1], mBinding.rootView1.getTop());
        mBinding.ll1.scrollBy(0, 200);
        mBinding.rootView1.getLocationInWindow(location);
        LogUtils.d(location[0], location[1], mBinding.rootView1.getTop());
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
