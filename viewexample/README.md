# 自定义View

## 概述
### 自定义View的基本方法
自定义View最基本的三个方法：onMeasure()、onLayout()、onDraw()；
View在Activity中显示出来要经过测量、布局、绘制三个步骤，分别对应三个方法：measure()、layout()和draw()
- 测量：onMeasure()决定View的大小
- 布局：onLayout()决定View在ViewGroup中的位置
- 绘制：onDraw()决定绘制这个View

### 自定义View分类

- 自定义View：只需要重写onMeasure()和onDraw()
- 自定义ViewGroup：只需要重写onMeasure()和onLayout()

## 自定义View基础

### 分类

- 单一视图：不包含子View，比如TextView
- 视图组：包含子View，比如LinearLayout

### View类简介

- View类是Android所有组件的基类
- View表现为显示在屏幕上的各种视图
- View的构造函数

```java
    // 该构造函数用来在代码中创建一个View实例
    public View(Context context) {
        // ...
    }

    // 该构造函数由Framework调用，为在XML里声明的View创建实例。
    // AttributeSet实例封装了在XML里声明的View标签的属性
    public View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // 子类可以在第二个构造函数里调用此构造函数，为自己设置默认的主题样式，例如R.attr.buttonStyle
    public View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    // 子类可以在第二个构造函数里调用此构造函数，为自己设置默认的主题样式，
    // 例如R.attr.buttonStyle。以及默认的样式，例如R.style.buttonStyle
    // 属性值按优先级依次从以下地方获取
    // 1.AttributeSet 中的属性
    // 2.AttributeSet 中的样式中
    // 3.主题中的样式中
    // 4.指定的默认样式
    public View(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context);
        // ...
    }

```
### AttributeSet与自定义属性

自定义属性的步骤
1.通过<declare-styleable>为自定义View声明属性
2.在XML里使用声明的属性
3.在构造函数里读取属性值
4.在View中应用属性值

### View的视图结构

1. PhoneWindow是Android系统的窗口实现类，继承自Window，负责管理界面显示及事件响应。
是Activity与View系统交互的接口。
2. DecorView是View树的根节点，组成了PhoneWindow，继承自FrameLayout
3. ViewRoot在Activity启动时创建，负责管理布局渲染窗口UI等等。

### Android坐标系

- 屏幕的左上角为坐标原点
- x轴从左到右增大
- y轴从上到下增大

### View的位置描述

**top, right, bottom, left以父控件为坐标轴的位置信息**

- top: 子View上边界到父View上边界的距离
- left: 子View左边界到父View左边界的距离
- bottom: 子View下边界到父View上边界的距离
- right: 子View右边界到父View左边界的距离

对应的API如下：
```java
    public final int getTop() {
        return mTop;
    }
    public final int getLeft() {
        return mLeft;
    }
    public final int getBottom() {
        return mBottom;
    }
    public final int getRight() {
        return mRight;
    }
```

### MotionEvent提供的方法

- getX(): 获取触摸点距**控件**左边界的距离
- getY(): 获取触摸点距**控件**上边界的距离
- getRawX(): 获取触摸点距**屏幕**左边界的距离
- getRawY(): 获取触摸点距**屏幕**上边界的距离

### 颜色

- ARGB8888：四通道高精度（32位）
- ARGB4444：四通道低精度（16位）
- RGB565：Android屏幕默认模式（16位）
- Alpha8：仅有透明通道（8位）

字母表示通道类型，数值表示通道类型用多少位二进制来描述。
例如ARGB8888，表示有四个通道ARGB，每个通道用8位来描述。
颜色值由小到大，值由浅到深

## View树的绘制流程

View树的绘制由ViewRoot负责，ViewRoot是View树的管理者，负责将DecorView和PhoneWindow组合起来，
DecorView与ViewRoot是一一对应的，WindowManager负责其对应关系。

### View树的添加

1. WindowManagerGlobal.addView
2. ViewRootImpl.setView
3. ViewRootImpl.requestLayout 
4. ViewRootImpl.scheduleTraversals 
5. TraversalRunnable.run
6. ViewRootImpl.doTraversal 
7. ViewRootImpl.performTraversals

### View树的绘制流程（ViewRootImpl.performTraversals）

1. 如果需要重新测量，则重新测量（measure），否则跳到2
2. 如果需要重新布局，则重新布局（layout），否则跳到3
3. 如果需要重新绘制，则重新绘制（draw），否则跳到4

### measure

## View 源码分析

```java
class View {
    // 获取默认测量大小的算法
    public static int getDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
        case MeasureSpec.UNSPECIFIED:
            // 父View对子View没有约束，返回子View的测量大小
            result = size;
            break;
        case MeasureSpec.AT_MOST:
            // 父View给出了子View的大小上界，直接用上界
        case MeasureSpec.EXACTLY:
            // 父View已经计算出了子View的大小，直接用
            result = specSize;
            break;
        }
        return result;
    }
    // 该方法用来保存测量后的宽高，并考虑阴影等效果
    protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
        boolean optical = isLayoutModeOptical(this);
        if (optical != isLayoutModeOptical(mParent)) {
            // 考虑要绘制的阴影效果
            Insets insets = getOpticalInsets();
            int opticalWidth  = insets.left + insets.right;
            int opticalHeight = insets.top  + insets.bottom;

            measuredWidth  += optical ? opticalWidth  : -opticalWidth;
            measuredHeight += optical ? opticalHeight : -opticalHeight;
        }
        // 调用该方法来保存测量好的宽度和高度
        setMeasuredDimensionRaw(measuredWidth, measuredHeight);
    }

    private void setMeasuredDimensionRaw(int measuredWidth, int measuredHeight) {
        // 将测量宽度和高度保存到如下成员中
        mMeasuredWidth = measuredWidth;
        mMeasuredHeight = measuredHeight;
        // 标记该View已经被测量过
        mPrivateFlags |= PFLAG_MEASURED_DIMENSION_SET;
    }
}

class ViewGroup {

    // spec 是父View自身的测量规则
    // padding 垂直或者水平方向的内边距
    // childDimension 子View宽度或者高度的布局参数(layout_width/layout_height)
    // 该方法根据父View的测量规则和子View的布局参数生成子View的测量规则 
    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        // 父View里的可用空间大小
        int size = Math.max(0, specSize - padding);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
        // 父View的大小固定
        case MeasureSpec.EXACTLY:
            if (childDimension >= 0) {
                // 子View在布局参数里指定了具体的数值，直接使用该值
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // 子View想要和父View的大小一样，则使用父View的大小和测量规则
                resultSize = size;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // 子View的大小取决于其内容的大小，则将父View的大小作为子View的大小上界
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            }
            break;

        // 父View的大小还不知道，但是有一个上界
        case MeasureSpec.AT_MOST:
            if (childDimension >= 0) {
                // 子View在布局参数里指定了具体的数值，直接使用该值
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // 子View想要和父View的大小一样，则使用父View的大小和测量规则
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // 子View的大小取决于其内容的大小，则将父View的大小上界作为子View的大小上界
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            }
            break;

        // 父View的大小还不知道，想要多大就有多大
        case MeasureSpec.UNSPECIFIED:
            if (childDimension >= 0) {
                // 子View在布局参数里指定了具体的数值，直接使用该值
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // 子View想要和父View的大小一样，则使用父View的大小和测量规则
                resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                resultMode = MeasureSpec.UNSPECIFIED;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // 子View的大小取决于其内容的大小，子View想要多大就有多大
                resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                resultMode = MeasureSpec.UNSPECIFIED;
            }
            break;
        }
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }
}
```

### onSizeChanged调用流程

只有当前View的大小发生改变时，调用onSizeChanged方法

```java
class View {

    // 摆放当前的View
    public void layout(int l, int t, int r, int b) {
        // ...
        int oldL = mLeft;
        int oldT = mTop;
        int oldB = mBottom;
        int oldR = mRight;
        // 保存位置
        boolean changed = isLayoutModeOptical(mParent) ?
                setOpticalFrame(l, t, r, b) : setFrame(l, t, r, b);
        if (changed || (mPrivateFlags & PFLAG_LAYOUT_REQUIRED) == PFLAG_LAYOUT_REQUIRED) {
            // 通知摆放子View
            onLayout(changed, l, t, r, b);
            // ...
        }
        // ...
    }

    // 保存当前View的大小和位置
    protected boolean setFrame(int left, int top, int right, int bottom) {
        boolean changed = false;
        if (mLeft != left || mRight != right || mTop != top || mBottom != bottom) {
            // 只有位置发生了改变才会保存
            changed = true;
            // ...
            int oldWidth = mRight - mLeft;
            int oldHeight = mBottom - mTop;
            int newWidth = right - left;
            int newHeight = bottom - top;
            boolean sizeChanged = (newWidth != oldWidth) || (newHeight != oldHeight);
            // ...
            // 保存位置
            mLeft = left;
            mTop = top;
            mRight = right;
            mBottom = bottom;
            mRenderNode.setLeftTopRightBottom(mLeft, mTop, mRight, mBottom);
            // ...
            if (sizeChanged) {
                // 如果宽高发生改变，发出通知
                sizeChange(newWidth, newHeight, oldWidth, oldHeight);
            }
            // ...
        }
        return changed;
    }

    private void sizeChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        // 通知大小发生改变
        onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        // ... 
    }
}
```