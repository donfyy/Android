package com.donfyy.example.constraintlayoutexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

/**
 * 被group控制的view，其可见性只能由该group来控制。
 * 多个group控制了同一个view的可见性，则在xml里最后定义的group具有控制权。
 */
public class MainActivity extends AppCompatActivity {

    private View mGroup;
    private boolean mIsGroupVisible = true;
    private View mTextView1;
    private boolean mIsTextView1Visible = true;
    private View mGroup1;
    private boolean mIsTextView1GroupVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGroup = findViewById(R.id.text_view_group);
        mTextView1 = findViewById(R.id.text_view_1);
        mGroup1 = findViewById(R.id.text_view_group_1);
    }

    public void showOrHideGroup(View view) {
        if (mIsGroupVisible) {
            mIsGroupVisible = false;
            mGroup.setVisibility(View.GONE);
        } else {
            mIsGroupVisible = true;
            mGroup.setVisibility(View.VISIBLE);
        }
    }

    public void showOrHideText1(View view) {
        if (mIsTextView1Visible) {
            mIsTextView1Visible = false;
            mTextView1.setVisibility(View.GONE);
        } else {
            mIsTextView1Visible = true;
            mTextView1.setVisibility(View.VISIBLE);
        }
    }

    public void showOrHideText1WithGroup(View view) {
        if (mIsTextView1GroupVisible) {
            mIsTextView1GroupVisible = false;
            mGroup1.setVisibility(View.GONE);
        } else {
            mIsTextView1GroupVisible = true;
            mGroup1.setVisibility(View.VISIBLE);
        }
    }
}
