package com.donfyy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.donfyy.c_scroll.case1.Case1Activity;
import com.donfyy.c_scroll.case2.Case2Activity;
import com.donfyy.c_scroll.case3.Case3Activity;
import com.donfyy.conflict.R;
import com.donfyy.willremove.intercept.innerintercept.InnerInterceptActivity;
import com.donfyy.d_innerouterconflict.outerinterceptlifthand.LiftHandActivity;
import com.donfyy.d_innerouterconflict.innerinterceptnolifthand.NoLiftHandActivity;
import com.donfyy.willremove.intercept.outerintercept.OuterInterceptActivity;
import com.donfyy.a_conflictdemo.ConflictActivity;
import com.donfyy.e_conflictfixcases.nestedscroll.NestedViewPagerActivity;
import com.donfyy.a_now.HeadlineNewsActivity;
import com.donfyy.b_viewarchitecture.TouchEventDemoActivity;
import com.donfyy.e_conflictfixcases.webview.WebviewActivity;
import com.donfyy.e_conflictfixcases.x5webview.X5WebviewActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.nested_scroll) {
            Intent intent = new Intent(this, NestedViewPagerActivity.class);
            startActivity(intent);
        }  else if (v.getId() == R.id.case1) {
            Intent intent = new Intent(this, Case1Activity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.case2) {
            Intent intent = new Intent(this, Case2Activity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.case3) {
            Intent intent = new Intent(this, Case3Activity.class);
            startActivity(intent);
        } /*else if (v.getId() == R.id.two_scroll) {
            Intent intent = new Intent(this, TwoScrollActivity.class);
            startActivity(intent);
        } */else if (v.getId() == R.id.nonlift) {
            Intent intent = new Intent(this, NoLiftHandActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.lift) {
            Intent intent = new Intent(this, LiftHandActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.conflict) {
            Intent intent = new Intent(this, ConflictActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.viewpager) {
            Intent intent = new Intent(this, HeadlineNewsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.event_dispatch) {
            Intent intent = new Intent(this, TouchEventDemoActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.outer_intercept) {
            Intent intent = new Intent(this, OuterInterceptActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.inner_intercept) {
            Intent intent = new Intent(this, InnerInterceptActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.webview_conflict) {
            Intent intent = new Intent(this, WebviewActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.x5webview_conflict) {
            Intent intent = new Intent(this, X5WebviewActivity.class);
            startActivity(intent);
        }
    }
}
