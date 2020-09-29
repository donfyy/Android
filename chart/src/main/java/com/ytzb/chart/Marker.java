package com.ytzb.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ytzb.chart.data.Data;
import com.ytzb.chart.data.Entry;
import com.ytzb.chart.data.Highlight;
import com.ytzb.chart.data.XAxis;
import com.ytzb.chart.dataset.IDataSet;

/**
 * Created by xinxin.wang on 18/4/28.
 */

@SuppressLint("ViewConstructor")
public class Marker extends FrameLayout implements IMarker {

    public Marker(Context context, int layoutResource) {
        super(context);
        setupLayoutResource(layoutResource);
    }

    /**
     * Sets the layout resource for a custom MarkerView.
     *
     * @param layoutResource
     */
    private void setupLayoutResource(int layoutResource) {

        View inflated = LayoutInflater.from(getContext()).inflate(layoutResource, this);

        inflated.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        inflated.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        // measure(getWidth(), getHeight());
        inflated.layout(0, 0, inflated.getMeasuredWidth(), inflated.getMeasuredHeight());
    }

    @Override
    public void draw(Canvas canvas, Highlight highlight, Chart<? extends Data<? extends IDataSet>> chart) {
        float xPx, yPx = chart.getViewport().contentTop();
        XAxis xAxis = chart.getXAxis();

        float mid = xAxis.getMin() + (xAxis.getMax() - xAxis.getMin()) / 2;
        if (highlight.getX() > mid) {
            xPx = chart.getViewport().contentLeft();
        } else {
            xPx = chart.getViewport().contentRight() - getMeasuredWidth();
        }

        int before = canvas.save();
        canvas.translate(xPx, yPx);
        draw(canvas);
        canvas.restoreToCount(before);
    }

    @Override
    public void bind(SparseArray<Entry> entries, Data<? extends IDataSet> data) {
        if (mOnBindListener != null) {
            mOnBindListener.onBind(this, entries, data);
        }

        measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    public interface OnBindListener {
        void onBind(Marker marker, SparseArray<Entry> entriesMap, Data<? extends IDataSet> data);
    }

    private OnBindListener mOnBindListener;

    public void setOnBindListener(OnBindListener onBindListener) {
        mOnBindListener = onBindListener;
    }

    private SparseArray<View> mIdMap;

    public <T extends View> T getView(int id) {
        if (mIdMap == null) {
            mIdMap = new SparseArray<>();
        }

        View view = mIdMap.get(id);

        if (view == null) {
            view = findViewById(id);
            if (view != null) {
                mIdMap.append(id, view);
            }
        }

        return ((T) view);
    }
}
