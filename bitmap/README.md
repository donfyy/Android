# Bitmap--位图

## 如何得到 bitmap 对象？

Bitmap 是 Android 系统中的图像处理中最重要类之一。
Bitmap 可以获取图像文件信息，对图像进行剪切、旋转、缩放，压缩等操作，并可以以指定格式保存图像文件。 

有两种方法可以创建 Bitmap 对象，
分别是通过 Bitmap.createBitmap() 和 BitmapFactory 的 decode 系列静态方法创建 Bitmap 对象。 

下面我们主要介绍 BitmapFactory 的 decode 方式创建 Bitmap 对象。

- decodeFile 从文件系统中加载
  - 通过 Intent 打开本地图片或照片
  - 根据 uri 获取图片的路径
  - 根据路径解析 Bitmap：Bitmap bm = BitmapFactory.decodeFile(path);
- decodeResource 以 R.drawable.xxx 的形式从本地资源中加载
  - Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.icon);
- decodeStream 从输入流加载
  - Bitmap bm = BitmapFactory.decodeStream(stream);
- decodeByteArray 从字节数组中加载
  - Bitmap bm = BitmapFactory.decodeByteArray(myByte,0,myByte.length);

## BitmapFactory.Options

- inSampleSize：这是表示采样大小。用于将图片缩小加载出来的，以免占用太大内存，适合缩略图。
- inJustDecodeBounds：当 inJustDecodeBounds 为 true 时，执行 decodexxx 方法时，
BitmapFactory 只会解析图片的原始宽高信息，并不会真正的加载图片
- inPreferredConfig：用于配置图片解码方式，对应的类型 Bitmap.Config。
如果非null，则会使用它来解码图片。默认值为是 Bitmap.Config.ARGB_8888
- inBitmap：在 Android 3.0 开始引入了 inBitmap 设置，通过设置这个参数，
在图片加载的时候可以使用之前已经创建了的 Bitmap，以便节省内存，避免再次创建一个Bitmap。
在 Android4.4，新增了允许 inBitmap 设置的图片与需要加载的图片的大小不同的情况，
只要 inBitmap 的图片比当前需要加载的图片大就好了。

通过 BitmapFactory.Options 的这些参数，我们就可以按一定的采样率来加载缩小后的图片，
然后在 ImageView 中使用缩小的图片这样就会降低内存占用避免【OOM】，提高了 Bitamp 加载时的性能。

这其实就是我们常说的图片尺寸压缩。尺寸压缩是压缩图片的像素，
一张图片所占内存大小的计算方式： **图片类型＊宽＊高**，通过改变三个值减小图片所占的内存，
防止OOM，当然这种方式可能会使图片失真 。

## Bitmap.Config

```java
public static enum Config {
    ALPHA_8,//每个像素占用1byte内存
    RGB_565,//每个像素占用2byte内存
    ARGB_4444,//每个像素占用2byte内存
    ARGB_8888;//每个像素占用4byte内存；默认模式
}
```

## BitmapFactory源码分析

没有配置限定符的资源是针对基准密度像素设计的。

预缩放资源

根据当前屏幕的密度，系统会使用特定于密度的资源。
如果没有相应密度的资源，系统会加载默认资源，并根据需要将其放大或缩小。

```java
class BitmapFactory {
    public static Bitmap decodeResource(Resources res, int id, Options opts) {
        validate(opts);
        Bitmap bm = null;
        InputStream is = null; 
        try {
            final TypedValue value = new TypedValue();
            // 获取资源对应的字节流，并拿到资源对应的信息
            is = res.openRawResource(id, value);
            // 将字节流解析成一个Bitmap对象
            bm = decodeResourceStream(res, value, is, null, opts);
        } catch (Exception e) {
            // ... 
        } finally {
            // ...
        }
        // ...
        return bm;
    }
    public static Bitmap decodeResourceStream(@Nullable Resources res, @Nullable TypedValue value,
            @Nullable InputStream is, @Nullable Rect pad, @Nullable Options opts) {
        validate(opts);
        if (opts == null) {
            opts = new Options();
        }

        if (opts.inDensity == 0 && value != null) {
            // 获取到图片对应的密度
            final int density = value.density;
            if (density == TypedValue.DENSITY_DEFAULT) {
                // 使用基准像素密度
                opts.inDensity = DisplayMetrics.DENSITY_DEFAULT;
            } else if (density != TypedValue.DENSITY_NONE) {
                // 如果可以对图片进行缩放，则使用图片对应的密度
                opts.inDensity = density;
            }
        }
        
        if (opts.inTargetDensity == 0 && res != null) {
            // 获取到目标密度
            opts.inTargetDensity = res.getDisplayMetrics().densityDpi;
        }
        
        // 解析图片
        return decodeStream(is, pad, opts);
    }
}
```

## ScaleGestureDetector 分析

监听缩放的核心原理在于以多个手指的中心点画圆，然后求出该圆的直径。具体算法如下：

1.得到当前按下的手指的数量

2.分别累加每个手指的xy轴的坐标值，除以手指的数量得到圆点坐标

3.分别累加每个手指的xy轴距离圆点的距离，除以手指的数量得到xy轴方向上平均距离

4.将xy轴方向上的平均距离分别乘以2，然后求出斜边的长度即为圆的直径。

5.此次手势计算出的直径与上次计算出的直径之比，即为缩放系数。


```java
class ScaleGestureDetector {
    public boolean onTouchEvent(MotionEvent event) {
        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onTouchEvent(event, 0);
        }

        mCurrTime = event.getEventTime();

        final int action = event.getActionMasked();

        // Forward the event to check for double tap gesture
        if (mQuickScaleEnabled) {
            mGestureDetector.onTouchEvent(event);
        }

        // 获取到当前手指数量
        final int count = event.getPointerCount();
        // 是否是触控笔按下
        final boolean isStylusButtonDown =
                (event.getButtonState() & MotionEvent.BUTTON_STYLUS_PRIMARY) != 0;

        final boolean anchoredScaleCancelled =
                mAnchoredScaleMode == ANCHORED_SCALE_MODE_STYLUS && !isStylusButtonDown;
        // 手势是否完成了
        final boolean streamComplete = action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_CANCEL || anchoredScaleCancelled;

        if (action == MotionEvent.ACTION_DOWN || streamComplete) {
            // Reset any scale in progress with the listener.
            // If it's an ACTION_DOWN we're beginning a new event stream.
            // This means the app probably didn't give us all the events. Shame on it.
            // 重置状态
            if (mInProgress) {
                mListener.onScaleEnd(this);
                mInProgress = false;
                mInitialSpan = 0;
                mAnchoredScaleMode = ANCHORED_SCALE_MODE_NONE;
            } else if (inAnchoredScaleMode() && streamComplete) {
                mInProgress = false;
                mInitialSpan = 0;
                mAnchoredScaleMode = ANCHORED_SCALE_MODE_NONE;
            }

            if (streamComplete) {
                return true;
            }
        }

        if (!mInProgress && mStylusScaleEnabled && !inAnchoredScaleMode()
                && !streamComplete && isStylusButtonDown) {
            // Start of a button scale gesture
            // 没有缩放，启用触控笔缩放，不在anchoredScale模式下，触摸手势没有结束，触控笔按下
            mAnchoredScaleStartX = event.getX();
            mAnchoredScaleStartY = event.getY();
            mAnchoredScaleMode = ANCHORED_SCALE_MODE_STYLUS;
            mInitialSpan = 0;
        }

        final boolean configChanged = action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_POINTER_UP ||
                action == MotionEvent.ACTION_POINTER_DOWN || anchoredScaleCancelled;

        final boolean pointerUp = action == MotionEvent.ACTION_POINTER_UP;
        final int skipIndex = pointerUp ? event.getActionIndex() : -1;

        // Determine focal point
        // 计算缩放的中心点
        float sumX = 0, sumY = 0;
        final int div = pointerUp ? count - 1 : count;
        final float focusX;
        final float focusY;
        if (inAnchoredScaleMode()) {
            // In anchored scale mode, the focal pt is always where the double tap
            // or button down gesture started
            focusX = mAnchoredScaleStartX;
            focusY = mAnchoredScaleStartY;
            if (event.getY() < focusY) {
                mEventBeforeOrAboveStartingGestureEvent = true;
            } else {
                mEventBeforeOrAboveStartingGestureEvent = false;
            }
        } else {
            for (int i = 0; i < count; i++) {
                if (skipIndex == i) continue;
                sumX += event.getX(i);
                sumY += event.getY(i);
            }

            focusX = sumX / div;
            focusY = sumY / div;
        }

        // Determine average deviation from focal point
        // 计算每个手指距中心点的平均距离
        float devSumX = 0, devSumY = 0;
        for (int i = 0; i < count; i++) {
            if (skipIndex == i) continue;

            // Convert the resulting diameter into a radius.
            devSumX += Math.abs(event.getX(i) - focusX);
            devSumY += Math.abs(event.getY(i) - focusY);
        }
        final float devX = devSumX / div;
        final float devY = devSumY / div;

        // Span is the average distance between touch points through the focal point;
        // i.e. the diameter of the circle with a radius of the average deviation from
        // the focal point.
        // 得到以中心点为原点以平均距离为半径的圆的直径
        final float spanX = devX * 2;
        final float spanY = devY * 2;
        final float span;
        if (inAnchoredScaleMode()) {
            span = spanY;
        } else {
            span = (float) Math.hypot(spanX, spanY);
        }

        // Dispatch begin/end events as needed.
        // If the configuration changes, notify the app to reset its current state by beginning
        // a fresh scale event stream.
        final boolean wasInProgress = mInProgress;
        // 把当前的中心点保存下来
        mFocusX = focusX;
        mFocusY = focusY;
        if (!inAnchoredScaleMode() && mInProgress && (span < mMinSpan || configChanged)) {
            mListener.onScaleEnd(this);
            mInProgress = false;
            mInitialSpan = span;
        }
        if (configChanged) {
            // 保存直径
            mPrevSpanX = mCurrSpanX = spanX;
            mPrevSpanY = mCurrSpanY = spanY;
            mInitialSpan = mPrevSpan = mCurrSpan = span;
        }

        final int minSpan = inAnchoredScaleMode() ? mSpanSlop : mMinSpan;
        if (!mInProgress && span >=  minSpan &&
                (wasInProgress || Math.abs(span - mInitialSpan) > mSpanSlop)) {
            // 如果不是在缩放并且直径超过缩放直径阈值
            mPrevSpanX = mCurrSpanX = spanX;
            mPrevSpanY = mCurrSpanY = spanY;
            mPrevSpan = mCurrSpan = span;
            mPrevTime = mCurrTime;
            // 询问是否要缩放
            mInProgress = mListener.onScaleBegin(this);
        }

        // Handle motion; focal point and span/scale factor are changing.
        if (action == MotionEvent.ACTION_MOVE) {
            mCurrSpanX = spanX;
            mCurrSpanY = spanY;
            mCurrSpan = span;

            boolean updatePrev = true;

            if (mInProgress) {
                // 通知正在缩放，并询问是否更新上一次求出的直径
                updatePrev = mListener.onScale(this);
            }

            if (updatePrev) {
                mPrevSpanX = mCurrSpanX;
                mPrevSpanY = mCurrSpanY;
                mPrevSpan = mCurrSpan;
                mPrevTime = mCurrTime;
            }
        }

        return true;
    }

    // 得到缩放系数
    public float getScaleFactor() {
        // ...
        // 前后直径之比即为缩放系数
        return mPrevSpan > 0 ? mCurrSpan / mPrevSpan : 1;
    }
}
```

屏幕分辨率：纵横方向上的像素点数

dpi：对角线上每英寸像素点数