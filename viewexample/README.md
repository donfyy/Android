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

View主要分为两类
- 单一视图：不包含子View，比如TextView
- 视图组：包含子View，比如LinearLayout


 