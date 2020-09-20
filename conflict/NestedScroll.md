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