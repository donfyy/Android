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

总结来说，懒加载需要考虑4个方面的因素
1.fragment自身的生命周期
    1.在onCreate时如果其他3个因素满足，则分发一次可见性，不满足则不分发
    2.在onResume与onPause中判断并分发生命周期的可见性
2.父fragment的可见性
    1.当父fragment的可见性发生变化通知给所有的子fragment
    2.默认情况下父fragment不可见
3.在ViewPager中的可见性
    1.当ViewPager的中心页发生变化时，通过setUserVisible通知fragment的可见性
4.页面切换hide与show的判断
    1.当通过FragmentTransaction隐藏与显示fragment的时候需要通知对应fragment的可见性
