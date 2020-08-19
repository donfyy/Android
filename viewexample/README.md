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
