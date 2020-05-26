package com.xiangxue.willremove.intercept.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xiangxue.conflict.R;
import com.xiangxue.willremove.intercept.utils.MyUtils;

import java.util.ArrayList;

public abstract class BaseInterceptActivity extends AppCompatActivity {
    private BaseSimpleViewPager mListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        Log.d(getTag(), "onCreate");
        initView();
    }

    private void initView() {
        LayoutInflater inflater = getLayoutInflater();
        mListContainer = findViewById(R.id.container);
        final int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) inflater.inflate(
                    getViewPagerViewLayout(), mListContainer, false);
            layout.getLayoutParams().width = screenWidth;
            ((TextView)layout.findViewById(R.id.title)).setText("第" + (i + 1) + "页");
            createList(layout);
            mListContainer.addView(layout);
        }
    }

    private void createList(ViewGroup layout) {
        ListView listView = layout.findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            datas.add("name " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.content_list_item, R.id.name, datas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Toast.makeText(BaseInterceptActivity.this, "click item",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(getTag(), "dispatchTouchEvent action:" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(getTag(), "onTouchEvent action:" + event.getAction());
        return super.onTouchEvent(event);
    }

    protected abstract String getTag();

    protected abstract int getViewPagerViewLayout();

    protected abstract int getActivityLayout();

}
