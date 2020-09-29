package com.ytzb.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.ytzb.chart.data.AxisDependency;
import com.ytzb.chart.data.Data;
import com.ytzb.chart.data.DataSet;
import com.ytzb.chart.data.Entry;
import com.ytzb.chart.data.Highlight;
import com.ytzb.chart.data.XAxis;
import com.ytzb.chart.data.YAxis;
import com.ytzb.chart.dataprovider.Provider;
import com.ytzb.chart.dataset.IDataSet;
import com.ytzb.chart.formatter.XAxisFormatter;
import com.ytzb.chart.renderer.DataRenderer;
import com.ytzb.chart.renderer.XAxisRenderer;
import com.ytzb.chart.renderer.YAxisRenderer;
import com.ytzb.chart.utils.Utils;
import com.ytzb.chart.utils.YPointF;

/**
 * Created by xinxin.wang on 18/4/23.
 * 图表假设了x轴上的逻辑单位值为1，平移与缩放的数量以1为单位进行舍入处理
 */

public class Chart<D extends Data<? extends IDataSet>> extends View implements Provider {

    public Chart(Context context) {
        super(context);
        init();
    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Chart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mBgLineWidth;
    private int mBgLineColor;
    private boolean mIsDrawBg;

    protected D mData;
    protected DataRenderer mDataRenderer;

    private XAxis mXAxis;
    private XAxisRenderer mXAxisRenderer;

    private YAxis mLYAxis;
    private YAxis mRYAxis;
    private YAxisRenderer mLYAxisRenderer;
    private YAxisRenderer mRYAxisRenderer;

    private Transformer mLTrans;
    private Transformer mRTrans;

    protected Viewport mViewport = new Viewport();

    private Highlight mLYAxisHighlight = new Highlight();
    private Highlight mRYAxisHighlight = new Highlight();

    private IMarker mMarker;

    private OnChartListener<D> mOnChartListener;
    private boolean mIsLoadingMoreFinished = true;
    private boolean mHasMore = true;

    private OnChartGestureListener mOnChartGestureListener;
    private GestureDispatcher mGestureDispatcher;

    private Runnable mHideHighlightTask = new Runnable() {
        @Override
        public void run() {
            if (mLYAxisHighlight.isEnable()) {
                mLYAxisHighlight.setEnable(false);
                mRYAxisHighlight.setEnable(false);

                invalidate();
            }
        }
    };

    private float mDefaultXAxisScalePx;
    private boolean mEnableXDrag;

    protected void init() {
        mLTrans = new Transformer(mViewport);
        mRTrans = new Transformer(mViewport);

        mLYAxis = new YAxis(AxisDependency.LEFT);
        mLYAxisRenderer = new YAxisRenderer(mViewport, mLYAxis, mLTrans);
        mRYAxis = new YAxis(AxisDependency.RIGHT);
        mRYAxisRenderer = new YAxisRenderer(mViewport, mRYAxis, mRTrans);

        mXAxis = new XAxis();
        mXAxis.setEnable(true);
        mXAxisRenderer = new XAxisRenderer(mViewport, mXAxis, mLTrans);

        mGestureDispatcher = new GestureDispatcher(this);

        mDefaultXAxisScalePx = Utils.dp2px(10, getContext());

        XAxisFormatter xAxisFormatter = new XAxisFormatter("0.00");
        mXAxis.setAxisFormatter(xAxisFormatter);
        mLYAxis.setAxisFormatter(xAxisFormatter);
        mRYAxis.setAxisFormatter(xAxisFormatter);

        mBgPaint.setColor(Color.GRAY);
        mBgPaint.setStrokeWidth(Utils.dp2px(1, getContext()));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDispatcher == null || mData == null) {
            return false;
        }
        return mGestureDispatcher.onTouch(this, event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mData == null) {
            return;
        }

        drawBg(canvas);

        mXAxisRenderer.renderAxisLine(canvas);
        mLYAxisRenderer.renderAxisLine(canvas);

        if (mRYAxis.isEnable()) {
            mRYAxisRenderer.renderAxisLine(canvas);
        }

        mXAxisRenderer.renderGridLines(canvas);
        mLYAxisRenderer.renderGridLines(canvas);

        if (mRYAxis.isEnable()) {
            mRYAxisRenderer.renderGridLines(canvas);
        }

        mXAxisRenderer.renderAxisValues(canvas);
        mLYAxisRenderer.renderAxisValues(canvas);

        if (mRYAxis.isEnable()) {
            mRYAxisRenderer.renderAxisValues(canvas);
        }

        mDataRenderer.renderData(canvas);

        if (isHighlight()) {
            mDataRenderer.renderHighlight(canvas, mLYAxisHighlight);

            mXAxisRenderer.renderHighlight(canvas, mLYAxisHighlight);
            mLYAxisRenderer.renderHighlight(canvas, mLYAxisHighlight);

            if (mRYAxis.isEnable()) {
                mRYAxisRenderer.renderHighlight(canvas, mRYAxisHighlight);
            }

            if (mMarker != null) {
                mMarker.draw(canvas, mLYAxisHighlight, this);
            }
        }

    }

    private void drawBg(Canvas canvas) {
        if (!mIsDrawBg) {
            return;
        }

        mBgPaint.setStrokeWidth(mBgLineWidth);
        mBgPaint.setColor(mBgLineColor);
        mBgPaint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(mViewport.contentRect(), mBgPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        boolean hasChartDimens = mViewport.hasChartDimens();

        mViewport.setChartDimens(w, h);

        if (mData != null) {

            if (!hasChartDimens) {
                calcDefaultXAxis();
            }

            notifyDataSetChanged();
        }
    }

    public void setData(D data) {
        mData = data;

        if (!mViewport.hasChartDimens()) {
            return;
        }
        calcDefaultXAxis();
        notifyDataSetChanged();
    }

    public void calcDefaultXAxis() {
        //默认刻度的数量
        float minX;
        float maxX;

        final float defaultXRange = (int) (mViewport.contentWidth() / mDefaultXAxisScalePx);

        if (mData.getMaxX() - mData.getMinX() > defaultXRange) {
            maxX = mData.getMaxXWithOffset();
            minX = mData.getMaxX() - defaultXRange - mData.getExtraMinXOffset();
        } else {
            minX = mData.getMinXWithOffset();
            maxX = mData.getMinX() + defaultXRange + mData.getExtraMaxXOffset();
        }

        mXAxis.calc(minX, maxX);
    }


    public void notifyDataSetChanged() {
        mData.calcMinMaxY(mXAxis.getMin(), mXAxis.getMax());

        mLYAxis.calc(mData.getMinY(AxisDependency.LEFT), mData.getMaxY(AxisDependency.LEFT));

        if (mRYAxis.isEnable()) {
            mRYAxis.calc(mData.getMinY(AxisDependency.RIGHT), mData.getMaxY(AxisDependency.RIGHT));
        }

        mXAxisRenderer.computeAxis(mXAxis.getMin(), mXAxis.getMax());
        mLYAxisRenderer.computeAxis(mLYAxis.getMin(), mLYAxis.getMax());
        if (mRYAxis.isEnable()) {
            mRYAxisRenderer.computeAxis(mRYAxis.getMin(), mRYAxis.getMax());
        }

        calcOffset();
    }

    private XAxis mFreezingXAxis;
    private Transformer mFreezingTrans;

    public void startDragX(MotionEvent event) {
        mFreezingXAxis = mXAxis.freeze();
        mFreezingTrans = mLTrans.freeze();
    }

    public void dragX(MotionEvent event, YPointF savedPoint) {
        if (mXAxis.getMin() <= mData.getMinXWithOffset() && mData.getMaxXWithOffset() <= mXAxis.getMax()) {
            return;
        }

        mPointBuffer[0] = savedPoint.x;
        mPointBuffer[2] = event.getX();
        mFreezingTrans.pixelsToValue(mPointBuffer);

        int offset = Math.round(mPointBuffer[2] - mPointBuffer[0]);

        if (offset == 0) {
            return;
        }

        if (offset > 0) {
            if (mXAxis.getMin() == mData.getMinXWithOffset()) {
                if (mHasMore && mIsLoadingMoreFinished && mOnChartListener != null) {
                    mIsLoadingMoreFinished = false;
                    mOnChartListener.onLoadingMore(this);
                }
                return;
            }

            float min = mFreezingXAxis.getMin() - offset;
            if (min < mData.getMinXWithOffset()) {
                offset = (int) (mFreezingXAxis.getMin() - mData.getMinXWithOffset());

                if (mHasMore && mIsLoadingMoreFinished && mOnChartListener != null) {
                    mIsLoadingMoreFinished = false;
                    mOnChartListener.onLoadingMore(this);
                }
            }

        } else {
            if (mXAxis.getMax() == mData.getMaxXWithOffset()) {
                return;
            }

            float max = mFreezingXAxis.getMax() - offset;
            if (max > mData.getMaxXWithOffset()) {
                offset = (int) (mFreezingXAxis.getMax() - mData.getMaxXWithOffset());
            }
        }

        mXAxis.calc(mFreezingXAxis.getMin() - offset, mFreezingXAxis.getMax() - offset);
        notifyDataSetChanged();
        invalidate();
    }

    private void calcOffset() {
        float offsetLeft = 0, offsetTop = 0, offsetBottom = 0, offsetRight = 0;

        if (mLYAxis.needOffset()) {
            offsetLeft += mLYAxis.getRequiredWidth(mLYAxisRenderer.getAxisLabelPaint());
        }

        if (mRYAxis.needOffset()) {
            offsetRight += mRYAxis.getRequiredWidth(mRYAxisRenderer.getAxisLabelPaint());
        }

        if (mXAxis.needOffset()) {
            offsetBottom += mXAxis.getRequiredHeight(mXAxisRenderer.getAxisLabelPaint());
        }

        offsetLeft += getPaddingLeft();
        offsetTop += getPaddingTop();
        offsetRight += getPaddingRight();
        offsetBottom += getPaddingBottom();

        mViewport.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);

        mLTrans.prepareOffsetMatrix();
        if (mRYAxis.isEnable()) {
            mRTrans.prepareOffsetMatrix();
        }

        mLTrans.prepareValueToPxMatrix(mXAxis.getMin(), mXAxis.getRange(), mLYAxis.getRange(), mLYAxis.getMin());

        if (mRYAxis.isEnable()) {
            mRTrans.prepareValueToPxMatrix(mXAxis.getMin(), mXAxis.getRange(), mRYAxis.getRange(), mRYAxis.getMin());
        }
    }

    public void stopDragX(MotionEvent event) {

    }

    private float[] mPointBuffer = new float[4];

    public void startHighlight(float xPx, float yPx) {
        removeCallbacks(mHideHighlightTask);

        highlight(xPx, yPx);
    }

    private SparseArray<Entry> mEntriesMapBuffer = new SparseArray<>();

    public void highlight(float xPx, float yPx) {

        mPointBuffer[0] = xPx;
        mPointBuffer[1] = yPx;
        mLTrans.pixelsToValue(mPointBuffer);

        if (mPointBuffer[0] < mXAxis.getMin()) {
            mData.getEntriesForXValue(mXAxis.getMin(), mPointBuffer[1], DataSet.Rounding.UP, mEntriesMapBuffer);
        } else if (mPointBuffer[0] > mXAxis.getMax()) {
            mData.getEntriesForXValue(mXAxis.getMax(), mPointBuffer[1], DataSet.Rounding.DOWN, mEntriesMapBuffer);
        } else {
            mData.getEntriesForXValue(mPointBuffer[0], mPointBuffer[1], DataSet.Rounding.CLOSEST, mEntriesMapBuffer);
        }

        float x = mEntriesMapBuffer.size() == 0 ? mPointBuffer[0] : mEntriesMapBuffer.valueAt(0).getX();
        float y = mPointBuffer[1];

        mPointBuffer[0] = x;
        mLTrans.pointValuesToPixel(mPointBuffer);

        float adjustedXPx = mPointBuffer[0];
        float adjustedYPx = mPointBuffer[1];

        mLYAxisHighlight.set(adjustedXPx, adjustedYPx, x, y);
        mLYAxisHighlight.setEnable(true);
        mLYAxisHighlight.setEntriesMap(mEntriesMapBuffer);

        mPointBuffer[0] = adjustedXPx;
        mPointBuffer[1] = adjustedYPx;
        mRTrans.pixelsToValue(mPointBuffer);
        mRYAxisHighlight.set(adjustedXPx, adjustedYPx, mPointBuffer[0], mPointBuffer[1]);
        mRYAxisHighlight.setEnable(true);
        mRYAxisHighlight.setEntriesMap(mEntriesMapBuffer);

        if (mMarker != null) {
            mMarker.bind(mEntriesMapBuffer, mData);
        }

        invalidate();
    }

    public boolean isHighlight() {
        return mLYAxisHighlight.isEnable() || mRYAxisHighlight.isEnable();
    }

    public void stopHighlight() {
        postDelayed(mHideHighlightTask, 3000);
    }

    public void singleTapUp() {
        if (removeCallbacks(mHideHighlightTask)) {
            post(mHideHighlightTask);
        }
    }

    @Override
    public Transformer getTransformer(AxisDependency axisDependency) {
        return axisDependency == AxisDependency.LEFT ? mLTrans : mRTrans;
    }

    public XAxis getXAxis() {
        return mXAxis;
    }

    public YAxis getYAxis(AxisDependency axisDependency) {
        if (axisDependency == AxisDependency.LEFT) {
            return mLYAxis;
        }

        return mRYAxis;
    }

    @Override
    public D getData() {
        return mData;
    }

    public void setOnChartListener(OnChartListener onChartListener) {
        mOnChartListener = onChartListener;
    }

    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
    }

    public void setLoadingMoreFinished(boolean loadingMoreFinished) {
        mIsLoadingMoreFinished = loadingMoreFinished;
    }

    public void setOnChartGestureListener(OnChartGestureListener onChartGestureListener) {
        mOnChartGestureListener = onChartGestureListener;
    }

    public OnChartGestureListener getOnChartGestureListener() {
        return mOnChartGestureListener;
    }

    public void setDefaultXAxisScalePx(float defaultXAxisScalePx) {
        mDefaultXAxisScalePx = defaultXAxisScalePx;
    }

    public void setEnableXDrag(boolean enableXDrag) {
        mEnableXDrag = enableXDrag;
    }

    public void setMarker(IMarker marker) {
        mMarker = marker;
    }

    public boolean isEnableXDrag() {
        return mEnableXDrag;
    }

    public Viewport getViewport() {
        return mViewport;
    }

    public float getBgLineWidth() {
        return mBgLineWidth;
    }

    public void setBgLineWidth(float bgLineWidth) {
        mBgLineWidth = bgLineWidth;
    }

    public int getBgLineColor() {
        return mBgLineColor;
    }

    public void setBgLineColor(int bgLineColor) {
        mBgLineColor = bgLineColor;
    }

    public boolean isDrawBg() {
        return mIsDrawBg;
    }

    public void setDrawBg(boolean drawBg) {
        mIsDrawBg = drawBg;
    }
}
