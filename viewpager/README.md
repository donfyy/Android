## 解决ViewPager无法包裹内容的问题

```kotlin
class MyViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            // 先去测量下每一个子View
            child.measure(widthMeasureSpec, getChildMeasureSpec(MeasureSpec.getSize(heightMeasureSpec),
                    paddingLeft + paddingRight, child.layoutParams.height))
            val h = child.measuredHeight
            // 记下最大高度
            if (h > height) {
                height = h
            }
        }
        if (height == 0) {
            height = MeasureSpec.getSize(heightMeasureSpec)
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)))
    }
}
```

## 主页懒加载

1.setUserVisibleHint会在生命周期方法开始之前调用，原因参见PagerAdapter源码
2.需要考虑的情况
    1.第一次显示ViewPager中的第一页
    2.左右切换页
    3.用户从第二个Activity中返回
