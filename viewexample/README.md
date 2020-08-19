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

