# 嵌套滑动

## 原理

### NestedScrollingChild 

```java
// 如果一个 View 想要拥有将嵌套滑动操作分发给父View的能力，那么该 View 应该实现这个接口
// 通常，实现该接口的 View 应该创建一个 NestedScrollingChildHelper 对象（helper），
// 然后将 View 中所有与 NestedScrollingChildHelper 同名的方法委托给对象 helper。
public interface NestedScrollingChild {
    // 启用或禁用嵌套滑动
    // 设置为true，意味着该View可以与某个祖先View开始嵌套滑动。
    // 如果当前 View 正在进行嵌套滑动，此时禁用嵌套滑动和调用stopNestedScroll()方法的效果一样。
    void setNestedScrollingEnabled(boolean enabled);
    
    // 判断当前View是否启用嵌套滑动。
    // 如果启用并且该View实现了嵌套滑动，那么在一个嵌套滑动过程中，
    // 该View将作为嵌套滑动的子View将与滑动相关的数据转发给嵌套滑动的父View
    boolean isNestedScrollingEnabled();
    
    // 在给定的坐标轴上开始嵌套滑动
    // 该View应当遵守一下约定：
    // 该View应该调用该方法进行嵌套滑动的初始化，如果是触摸滑动应该在down事件发生时调用该方法。
    // 如果是触摸滑动，当子View请求该View不拦截事件时，嵌套滑动将自动终止。
    // 如果是使用代码触发的嵌套滑动，调用者必须在嵌套滑动结束时调用stopNestedScroll方法。
    // 返回true表示找到了一个嵌套滑动父View，嵌套滑动开始了
    // 返回false表示找不到嵌套滑动父View，因此不用进行嵌套滑动了，该View自己任意消费滑动事件。
    // 如果嵌套滑动正在进行，调用该方法总是返回true
    // 当嵌套滑动正在进行时，每滑动一步，当该View计算出滑动距离后，应该调用dispatchNestedPreScroll，
    // 如果dispatchNestedPreScroll方法返回true，表示父View消费了部分或者全部滑动距离，
    // 该View需要计算剩余的滑动距离。当该View消费了部分或者全部滑动距离后，应该调用dispatchNestedScroll方法，
    // 通知父View已经消费的和还未被消费的滑动距离，父View再根据自己的情况进行处理。
    boolean startNestedScroll(@ScrollAxis int axes);
    
    // 停止正在进行中的嵌套滑动。
    // 一般是在事件结束比如ACTION_UP或者ACTION_CANCEL中调用,告诉父布局滑动结束。
    void stopNestedScroll();
    
    // 返回true表示该View有一个嵌套滑动父View，
    // 这意味着嵌套滑动的初始化已经完成了，并且有一个祖先View参与嵌套滑动
    boolean hasNestedScrollingParent();
    
    // 分发嵌套滑动的一步。
    // 如果正在进行嵌套滑动，该View应该调用该方法通知嵌套滑动父View有关这一次滑动的信息。
    // 如果没有进行嵌套滑动，也没有启用嵌套滑动，调用该方法没有任何效果。
    // 该View应该在消费滑动距离之前调用dispatchNestedPreScroll
    // dxConsumed：被该View消费了的水平方向的滑动距离，以像素为单位
    // dyConsumed：被该View消费了的垂直方向的滑动距离
    // dxUnconsumed：未被消费的水平方向的滑动距离
    // dyUnconsumed：未被消费的垂直方向的滑动距离
    // offsetInWindow：输出可选参数。如果不是null，该方法完成返回时，
    // 会将该视图从该操作之前到该操作完成之后的本地视图坐标中的偏移量封装进该参数中，
    // offsetInWindow[0]水平方向，offsetInWindow[1]垂直方向
    // true：表示滑动事件被分发成功,false: 分发失败
    boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
            int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow);
    
    // 分发嵌套滑动的一步，在这一步开始后，该View消费任何滑动距离前进行。
    // 也就是说，让嵌套滑动父View优先消费滑动距离。
	// dx：水平方向的滑动距离，以像素为单位
	// dy：垂直方向的滑动距离
	// consumed：输出参数，嵌套滑动父View消费的滑动距离，consumed[0]代表水平方向，consumed[1]代表垂直方向
	// 返回true表示嵌套滑动父View消费了部分或全部的滑动距离
    boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed,
            @Nullable int[] offsetInWindow);
    
    // 将惯性滑动分发给嵌套滑动父View。
    // velocityX：水平滑动速度
    // velocityY：垂直滑动速度
    // consumed：true表示该View消费了惯性滑动，否则传入false
    // true：表示嵌套滑动父View消费了惯性滑动
    boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed);
    
    // 在该View消费惯性滑动前，先将惯性滑动分发给嵌套滑动父View。
    // 该事件不能共享，如果嵌套滑动父View消费了，该View就不可以再消费了
    // 如果该View要自己消费掉该事件，就不要调用该方法了。
    // 该方法给嵌套滑动父View提供了一个机会，在该View消费惯性滑动之前，让嵌套滑动父View选择是否完全消费掉惯性滑动
    // 返回false，代表Parent没有处理，但是不代表Parent后面就不用处理了
    // true：表示嵌套滑动父View消费了惯性滑动 速度是以像素每秒为单位。
    boolean dispatchNestedPreFling(float velocityX, float velocityY);
}
@IntDef({TYPE_TOUCH, TYPE_NON_TOUCH})
public @interface NestedScrollType {}
// NestedScrollingChild2为NestedScrollingChild的一些同名方法增加了一个
// 表示当前嵌套滑动类型的参数
public interface NestedScrollingChild2 extends NestedScrollingChild {

    // type表示嵌套滑动的类型 触摸滑动还是由程序触发的嵌套滑动
    boolean startNestedScroll(@ScrollAxis int axes, @NestedScrollType int type);
    void stopNestedScroll(@NestedScrollType int type);
    boolean hasNestedScrollingParent(@NestedScrollType int type);
    boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
            int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow,
            @NestedScrollType int type);
    boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed,
            @Nullable int[] offsetInWindow, @NestedScrollType int type);

}
// 3为2的同名方法多增加了一个参数
public interface NestedScrollingChild3 extends NestedScrollingChild2 {

    // consumed表示一个初始值加上嵌套滑动父View消费的滑动距离
    void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
            @Nullable int[] offsetInWindow, @ViewCompat.NestedScrollType int type,
            @NonNull int[] consumed);
}
```

### NestedScrollingParent
```java
// 如果一个ViewGroup想要参与嵌套滑动，那么该ViewGroup应该实现该接口。
// 通常，实现了该接口的ViewGroup应该创建一个NestedScrollingParentHelper对象（helper），
// 然后将View或ViewGroup中所有与NestedScrollingParentHelper同名的方法委托给对象 helper
public interface NestedScrollingParent {

    // 收到该通知表示，一个子孙View（target）要开始嵌套滑动了，ViewGroup在这个方法中判断是不是要参与嵌套滑动。
    // 该方法在子孙View调用startNestedScroll方法后调用。
    // child：该ViewGroup包含target的直接子View，如果只有一层嵌套，和target是同一个View
    // target：发起嵌套滑动的子孙View
    // 返回true表示该ViewGroup参与嵌套滑动，在嵌套滑动期间该ViewGroup称为target的嵌套滑动父View。
    boolean onStartNestedScroll(@NonNull View child, @NonNull View target, @ScrollAxis int axes);

    // 收到该通知表示，ViewGroup成功参与了嵌套滑动，ViewGroup可以在这个方法中初始化与嵌套滑动相关的状态
    // 如果onStartNestedScroll()方法返回的是true的话,那么紧接着就会调用该方法。
    void onNestedScrollAccepted(@NonNull View child, @NonNull View target, @ScrollAxis int axes);

    // 该方法表示，嵌套滑动要停止了，ViewGroup可以在该方法内清理与嵌套滑动有关的状态。
    // 当target调用stopNestedScroll()时会调用该方法
    void onStopNestedScroll(@NonNull View target);

    // 该通知表示，正在进行一步嵌套滑动。
    // 当子view调用dispatchNestedScroll()方法时,会调用该方法。也就是开始分发处理嵌套滑动了
    // dxConsumed：已经被target消费掉的水平方向的滑动距离
    // dyConsumed：已经被target消费掉的垂直方向的滑动距离
    // dxUnconsumed：未被target消费掉的水平方向的滑动距离
    // dyUnconsumed：未被target消费掉的垂直方向的滑动距离
    void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
            int dxUnconsumed, int dyUnconsumed);

    // 该通知表示，要进行一步嵌套滑动了，并且target没有消费滑动距离。
    // 该方法在target调用dispatchNestedPreScroll()方法后调用。
    // 如果ViewGroup想先消费部分滑动距离，将消费的距离放入consumed数组
    // dx：水平滑动距离
    // dy：垂直滑动距离
    // consumed：表示ViewGroup消费的滑动距离,consumed[0]和consumed[1]分别表示在x和y方向上消费的距离.
    void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed);

    // 该通知表示，惯性滑动开始了。
    // velocityX：水平方向的滑动速度
    // velocityY：垂直方向的滑动速度
    // consumed：是否被target消费了
    // 返回true表示ViewGroup消费了惯性滑动
    boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed);

    //  该通知表示，惯性滑动开始了，并且target还没有消费惯性滑动。
    //  在惯性滑动距离处理之前，会调用该方法，同onNestedPreScroll()一样，也是给ViewGroup优先处理的权利
    //  target：本次嵌套滑动的NestedScrollingChild
    //  velocityX：水平方向的滑动速度
    //  velocityY：垂直方向的滑动速度
    //  返回true表示ViewGroup消费了惯性滑动，同时也表示target不应该再处理了。
    boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY);

    // 返回该ViewGroup滑动的方向
    @ScrollAxis
    int getNestedScrollAxes();
}
```

```java
// 2为1的一些同名方法增加一个表示当前滑动类型的参数
public interface NestedScrollingParent2 extends NestedScrollingParent {

    boolean onStartNestedScroll(@NonNull View child, @NonNull View target, @ScrollAxis int axes,
            @NestedScrollType int type);
    void onNestedScrollAccepted(@NonNull View child, @NonNull View target, @ScrollAxis int axes,
            @NestedScrollType int type);
    void onStopNestedScroll(@NonNull View target, @NestedScrollType int type);
    void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
            int dxUnconsumed, int dyUnconsumed, @NestedScrollType int type);
    void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed,
            @NestedScrollType int type);
}
```

```java
public interface NestedScrollingParent3 extends NestedScrollingParent2 {
    // ViewGroup需要将自己消费的滑动距离 追加 进consumed数组。
    // 因此consumed数组中的值表示所有的嵌套滑动父View消费的总距离
    void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
            int dyUnconsumed, @ViewCompat.NestedScrollType int type, @NonNull int[] consumed);

}
```

### NestedScrollingChildHelper

```java
// framework实现的一个标准的嵌套滑动策略
// 实现了NestedScrollingChild的View，应该创建一个该类的对象，然后将所有同名方法委托给该对象。
// 这个类的实现比较简单，都是一些转发操作。
// 在startNestedScroll方法中，找到第一个参与本次嵌套滑动的祖先View，
// 找到之后，在对应的分发方法中，将相关参数分发到parent中与之对应的方法中。
// 而且为了兼容性，都是通过ViewParentCompat进行转发操作的
public class NestedScrollingChildHelper {
    private ViewParent mNestedScrollingParentTouch;
    private ViewParent mNestedScrollingParentNonTouch;
    private final View mView;
    private boolean mIsNestedScrollingEnabled;
    private int[] mTempNestedScrollConsumed;

 
	// 传一个View进来，该View实现了NestedScrollingChild接口
    public NestedScrollingChildHelper(@NonNull View view) {
        mView = view;
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        // 记录是否启用嵌套滑动。
        // 如果调用前已经启用了嵌套滑动，则会停止当前可能的嵌套滑动
        if (mIsNestedScrollingEnabled) {
            ViewCompat.stopNestedScroll(mView);
        }
        mIsNestedScrollingEnabled = enabled;
    }
    // ...
    public boolean startNestedScroll(@ScrollAxis int axes) {
        return startNestedScroll(axes, TYPE_TOUCH);
    }

    public boolean startNestedScroll(@ScrollAxis int axes, @NestedScrollType int type) {
        if (hasNestedScrollingParent(type)) {
            // 当前正在进行嵌套滑动，不用处理
            return true;
        }
        if (isNestedScrollingEnabled()) {
            // 从父View开始向上找到第一个参与嵌套滑动的祖先View
            ViewParent p = mView.getParent();
            View child = mView;
            while (p != null) {
                if (ViewParentCompat.onStartNestedScroll(p, child, mView, axes, type)) {
                    // 将参与嵌套滑动的祖先View记录下来
                    setNestedScrollingParentForType(type, p);
                    // 通知p，p已经成功的参与了嵌套滑动，连接建立了[旺柴]
                    ViewParentCompat.onNestedScrollAccepted(p, child, mView, axes, type);
                    return true;
                }
                // ...
                p = p.getParent();
            }
        }
        return false;
    }

    public void stopNestedScroll() {
        stopNestedScroll(TYPE_TOUCH);
    }

    public void stopNestedScroll(@NestedScrollType int type) {
        ViewParent parent = getNestedScrollingParentForType(type);
        if (parent != null) {
            // 调用parent的onStopNestedScroll(target, type)方法
            ViewParentCompat.onStopNestedScroll(parent, mView, type);
            // 这次嵌套滑动结束了，将mNestedScrollingParentTouch置空，移除parent
            setNestedScrollingParentForType(type, null);
        }
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
            int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        return dispatchNestedScrollInternal(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow, TYPE_TOUCH, null);
    }

    // ...

    private boolean dispatchNestedScrollInternal(int dxConsumed, int dyConsumed,
            int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow,
            @NestedScrollType int type, @Nullable int[] consumed) {
        if (isNestedScrollingEnabled()) {
            final ViewParent parent = getNestedScrollingParentForType(type);
            if (parent == null) {
                return false;
            }
            // 判断是否是有效的嵌套滑动
            if (dxConsumed != 0 || dyConsumed != 0 || dxUnconsumed != 0 || dyUnconsumed != 0) {
                int startX = 0;
                int startY = 0;
                if (offsetInWindow != null) {
                    mView.getLocationInWindow(offsetInWindow);
                    startX = offsetInWindow[0];
                    startY = offsetInWindow[1];
                }
                if (consumed == null) {
                    consumed = getTempNestedScrollConsumed();
                    consumed[0] = 0;
                    consumed[1] = 0;
                }
                ViewParentCompat.onNestedScroll(parent, mView,
                        dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
                if (offsetInWindow != null) {
                    mView.getLocationInWindow(offsetInWindow);
                    offsetInWindow[0] -= startX;
                    offsetInWindow[1] -= startY;
                }
                return true;
            } else if (offsetInWindow != null) {
                offsetInWindow[0] = 0;
                offsetInWindow[1] = 0;
            }
        }
        return false;
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed,
            @Nullable int[] offsetInWindow) {
        return dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, TYPE_TOUCH);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed,
            @Nullable int[] offsetInWindow, @NestedScrollType int type) {
        if (isNestedScrollingEnabled()) {
            final ViewParent parent = getNestedScrollingParentForType(type);
            if (parent == null) {
                return false;
            }

            if (dx != 0 || dy != 0) {
                int startX = 0;
                int startY = 0;
                if (offsetInWindow != null) {
                    mView.getLocationInWindow(offsetInWindow);
                    startX = offsetInWindow[0];
                    startY = offsetInWindow[1];
                }

                if (consumed == null) {
                    consumed = getTempNestedScrollConsumed();
                }
                consumed[0] = 0;
                consumed[1] = 0;
                // 这里会调用ViewParent的onNestedPreScroll()方法 Parent消费的数据会缝在consumed变量中
                ViewParentCompat.onNestedPreScroll(parent, mView, dx, dy, consumed, type);

                if (offsetInWindow != null) {
                    mView.getLocationInWindow(offsetInWindow);
                    offsetInWindow[0] -= startX;
                    offsetInWindow[1] -= startY;
                }
                return consumed[0] != 0 || consumed[1] != 0;
            } else if (offsetInWindow != null) {
                offsetInWindow[0] = 0;
                offsetInWindow[1] = 0;
            }
        }
        return false;
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        if (isNestedScrollingEnabled()) {
            ViewParent parent = getNestedScrollingParentForType(TYPE_TOUCH);
            if (parent != null) {
                // 这里会调用parent的onNestedFling()方法
                return ViewParentCompat.onNestedFling(parent, mView, velocityX,
                        velocityY, consumed);
            }
        }
        return false;
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        if (isNestedScrollingEnabled()) {
            ViewParent parent = getNestedScrollingParentForType(TYPE_TOUCH);
            if (parent != null) {
                return ViewParentCompat.onNestedPreFling(parent, mView, velocityX,
                        velocityY);
            }
        }
        return false;
    }

    // 从窗口中分离的时候要停止嵌套滑动
    public void onDetachedFromWindow() {
        ViewCompat.stopNestedScroll(mView);
    }

    // ...
}
```

### NestedScrollingParentHelper 

```java
// 这个Helper更简单了，只是记录了嵌套滑动的类型
public class NestedScrollingParentHelper {
    private int mNestedScrollAxesTouch;
    private int mNestedScrollAxesNonTouch;

    public NestedScrollingParentHelper(@NonNull ViewGroup viewGroup) {
    }

    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target,
            @ScrollAxis int axes) {
        onNestedScrollAccepted(child, target, axes, ViewCompat.TYPE_TOUCH);
    }

    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target,
            @ScrollAxis int axes, @NestedScrollType int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            mNestedScrollAxesNonTouch = axes;
        } else {
            mNestedScrollAxesTouch = axes;
        }
    }

    @ScrollAxis
    public int getNestedScrollAxes() {
        return mNestedScrollAxesTouch | mNestedScrollAxesNonTouch;
    }

    public void onStopNestedScroll(@NonNull View target) {
        onStopNestedScroll(target, ViewCompat.TYPE_TOUCH);
    }

    public void onStopNestedScroll(@NonNull View target, @NestedScrollType int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            mNestedScrollAxesNonTouch = ViewGroup.SCROLL_AXIS_NONE;
        } else {
            mNestedScrollAxesTouch = ViewGroup.SCROLL_AXIS_NONE;
        }
    }
}
```

### NestedScrollView

```java
// NestedScrollView像一个ScrollView，但是NestedScrollView支持嵌套滑动，
// 既可以作为嵌套滑动子View也可以作为嵌套滑动父View。
// 作为嵌套滑动父View，NestedScrollView只是做了一层转发，再把嵌套滑动的事件转发给祖先View
public class NestedScrollView extends FrameLayout implements NestedScrollingParent3,
        NestedScrollingChild3, ScrollingView {
    private final NestedScrollingParentHelper mParentHelper;
    private final NestedScrollingChildHelper mChildHelper;

    public NestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // ...
        // 在构造方法中创建两个helper对象
        mParentHelper = new NestedScrollingParentHelper(this);
        mChildHelper = new NestedScrollingChildHelper(this);
        // 启用嵌套滑动
        // ...because why else would you be using this widget?
        setNestedScrollingEnabled(true);
        // ...
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // ...
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                // ...
                // 记下滑动起点坐标
                mLastMotionY = (int) ev.getY();
                mActivePointerId = ev.getPointerId(0);
                // 开始嵌套滑动
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                final int y = (int) ev.getY(activePointerIndex);
                int deltaY = mLastMotionY - y;
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaY > 0) { // 修正滑动距离
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    // 开始滑动一小步
                    if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset,
                            ViewCompat.TYPE_TOUCH)) {
                        // 计算还剩下的滑动距离
                        deltaY -= mScrollConsumed[1];
                        // 记下父View滑动后，该View相对于窗口的滑动距离
                        mNestedYOffset += mScrollOffset[1];
                    }

                    mLastMotionY = y - mScrollOffset[1];

                    final int oldY = getScrollY();  // 当前已经滑动的距离
                    final int range = getScrollRange(); // 可以滑动的最大距离
                    final int overscrollMode = getOverScrollMode();
                    boolean canOverscroll = overscrollMode == View.OVER_SCROLL_ALWAYS
                            || (overscrollMode == View.OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

                    // 消费剩余的滑动距离
                    if (overScrollByCompat(0, deltaY, 0, getScrollY(), 0, range, 0,
                            0, true) && !hasNestedScrollingParent(ViewCompat.TYPE_TOUCH)) {
                        // Break our velocity if we hit a scroll barrier.
                        mVelocityTracker.clear();
                    }
                    // 得到消费的滑动距离
                    final int scrolledDeltaY = getScrollY() - oldY;
                    // 得到没有消费的滑动距离
                    final int unconsumedY = deltaY - scrolledDeltaY;
                    // 重置consumed数组
                    mScrollConsumed[1] = 0;
                    // 通知嵌套滑动父View消费剩余的滑动距离
                    dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset,
                            ViewCompat.TYPE_TOUCH, mScrollConsumed);

                    mLastMotionY -= mScrollOffset[1];  // 没看懂这个更新
                    mNestedYOffset += mScrollOffset[1];

                    if (canOverscroll) {
                        // 消费剩余的滑动距离
                        deltaY -= mScrollConsumed[1];
                        // ...
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起时检测到惯性滑动
                if ((Math.abs(initialVelocity) >= mMinimumVelocity)) {
                    // 通知惯性滑动
                    if (!dispatchNestedPreFling(0, -initialVelocity)) {
                        // 如果父View没有消费，通知父View自己消费了
                        dispatchNestedFling(0, -initialVelocity, true);
                        fling(-initialVelocity);
                    }
                } 
                // ...
                break;
            // ...
        }
        // ...
        return true;
    }

    boolean overScrollByCompat(int deltaX, int deltaY,
            int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY,
            int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent) {
       final int overScrollMode = getOverScrollMode();
       final boolean canScrollVertical =
                computeVerticalScrollRange() > computeVerticalScrollExtent();
       final boolean overScrollVertical = overScrollMode == View.OVER_SCROLL_ALWAYS
                || (overScrollMode == View.OVER_SCROLL_IF_CONTENT_SCROLLS && canScrollVertical);
        // ...
        int newScrollY = scrollY + deltaY; // 计算出新的滑动位置
        if (!overScrollVertical) {
            maxOverScrollY = 0;
        }
        // Clamp values if at the limits and record
        final int left = -maxOverScrollX;
        final int right = maxOverScrollX + scrollRangeX;
        final int top = -maxOverScrollY;
        final int bottom = maxOverScrollY + scrollRangeY;
        // ...
        boolean clampedY = false;
        if (newScrollY > bottom) {
            // 新的滑动位置超出了位置上限
            // 将新的滑动位置更新为上限
            newScrollY = bottom; 
            clampedY = true;
        } else if (newScrollY < top) {
            // 新的滑动位置超出了位置下限
            // 将新的滑动位置更新为下限
            newScrollY = top;
            clampedY = true;
        }

        if (clampedY && !hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH)) {
            mScroller.springBack(newScrollX, newScrollY, 0, 0, 0, getScrollRange());
        }

        // 该方法调用了scrollTo方法，将内容滑动到新的位置newScrollY
        onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);

        return clampedX || clampedY;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes,
            int type) {
        // 如果时y轴方向上的滚动就参与嵌套滑动
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes,
            int type) {
        mParentHelper.onNestedScrollAccepted(child, target, axes, type);
        // 作为NestedScrollingChild再去向上寻找参与嵌套滑动的祖先View
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mParentHelper.onStopNestedScroll(target, type);
        stopNestedScroll(type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
            int dxUnconsumed, int dyUnconsumed, int type) {
        onNestedScrollInternal(dyUnconsumed, type, null);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed,
            int type) {
        dispatchNestedPreScroll(dx, dy, consumed, null, type);
    }

    @Override
    public boolean onNestedFling(
            @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            // 如果target没有消费，就自己消费。。。
            dispatchNestedFling(0, velocityY, true);
            fling((int) velocityY);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        // 直接转发
        return dispatchNestedPreFling(velocityX, velocityY);
    }
}
```

## 二级嵌套滑动实现

```kotlin
// 1 自定义NestedScrollLayout继承自NestedScrollView当作嵌套滑动的父容器
// 嵌套滑动通常分成两部分，内容区域的菜单
class NestedScrollLayout
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0)
    : NestedScrollView(context, attrs, defStyleAttr) {
    // 头部View的父容器
    private lateinit var headerView: View
    // 内容区域的父容器
    private lateinit var contentView: ViewGroup
 
} 
```