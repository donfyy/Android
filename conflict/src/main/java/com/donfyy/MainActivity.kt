package com.donfyy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.donfyy.a_conflictdemo.ConflictActivity
import com.donfyy.a_now.HeadlineNewsActivity
import com.donfyy.b_viewarchitecture.TouchEventDemoActivity
import com.donfyy.c_scroll.case1.Case1Activity
import com.donfyy.c_scroll.case2.Case2Activity
import com.donfyy.c_scroll.case3.Case3Activity
import com.donfyy.conflict.R
import com.donfyy.d_innerouterconflict.innerinterceptnolifthand.NoLiftHandActivity
import com.donfyy.d_innerouterconflict.outerinterceptlifthand.LiftHandActivity
import com.donfyy.e_conflictfixcases.nestedscroll.NestedViewPagerActivity
import com.donfyy.e_conflictfixcases.webview.WebviewActivity
import com.donfyy.e_conflictfixcases.x5webview.X5WebviewActivity
import com.donfyy.willremove.intercept.innerintercept.InnerInterceptActivity
import com.donfyy.willremove.intercept.outerintercept.OuterInterceptActivity

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)
    }

    fun onButtonClick(v: View) {
        when (v.id) {
            R.id.nested_scroll -> {
                val intent = Intent(this, NestedViewPagerActivity::class.java)
                startActivity(intent)
            }
            R.id.case1 -> {
                val intent = Intent(this, Case1Activity::class.java)
                startActivity(intent)
            }
            R.id.case2 -> {
                val intent = Intent(this, Case2Activity::class.java)
                startActivity(intent)
            }
            R.id.case3 -> {
                val intent = Intent(this, Case3Activity::class.java)
                startActivity(intent)
            } /*else if (v.getId() == R.id.two_scroll) {
            Intent intent = new Intent(this, TwoScrollActivity.class);
            startActivity(intent);
        } */
            R.id.nonlift -> {
                val intent = Intent(this, NoLiftHandActivity::class.java)
                startActivity(intent)
            }
            R.id.lift -> {
                val intent = Intent(this, LiftHandActivity::class.java)
                startActivity(intent)
            }
            R.id.conflict -> {
                val intent = Intent(this, ConflictActivity::class.java)
                startActivity(intent)
            }
            R.id.viewpager -> {
                val intent = Intent(this, HeadlineNewsActivity::class.java)
                startActivity(intent)
            }
            R.id.event_dispatch -> {
                val intent = Intent(this, TouchEventDemoActivity::class.java)
                startActivity(intent)
            }
            R.id.outer_intercept -> {
                val intent = Intent(this, OuterInterceptActivity::class.java)
                startActivity(intent)
            }
            R.id.inner_intercept -> {
                val intent = Intent(this, InnerInterceptActivity::class.java)
                startActivity(intent)
            }
            R.id.webview_conflict -> {
                val intent = Intent(this, WebviewActivity::class.java)
                startActivity(intent)
            }
            R.id.x5webview_conflict -> {
                val intent = Intent(this, X5WebviewActivity::class.java)
                startActivity(intent)
            }
        }
    }
}