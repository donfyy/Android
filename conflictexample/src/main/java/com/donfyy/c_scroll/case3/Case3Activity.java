package com.donfyy.c_scroll.case3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.donfyy.conflict.R;


/**
 * View的滚动（touch事件控制滚动）
 */
public class Case3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.case3_layout);

        bindEvent();
    }

    private void bindEvent() {
        final Case3ViewGroup containerViewGroup = (Case3ViewGroup) findViewById(R.id.container_viewgroup);
        Button leftButton = (Button) findViewById(R.id.button_left);
        Button rightButton = (Button) findViewById(R.id.button_right);
        View.OnClickListener onButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentIndex = containerViewGroup.getCurrentIndex();
                int targetIndex = currentIndex;
                switch (v.getId()) {
                    case R.id.button_left:
                        targetIndex = currentIndex - 1;
                        break;
                    case R.id.button_right:
                        targetIndex = currentIndex + 1;
                        break;
                }
                containerViewGroup.moveToIndex(targetIndex);
            }
        };
        leftButton.setOnClickListener(onButtonClickListener);
        rightButton.setOnClickListener(onButtonClickListener);
    }


}
