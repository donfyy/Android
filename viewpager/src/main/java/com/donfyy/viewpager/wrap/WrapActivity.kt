package com.donfyy.viewpager.wrap

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import java.util.*

class WrapActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var radioGroup: RadioGroup
    private lateinit var images: MutableList<Int>
    private var index = 0
    private var preIndex = 0
    private val timer = Timer()
    private val isContinue = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bannerlayout)
        viewPager = findViewById(R.id.view_Pager)
        radioGroup = findViewById(R.id.radio_group)
        images = ArrayList()
        images.add(R.drawable.p1)
        images.add(R.drawable.p2)
        images.add(R.drawable.p3)
        images.add(R.drawable.girl5)
        val pagerAdapter = MyPagerAdapter(images, this)
        viewPager.pageMargin = 30
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(onPageChangeListener)
        viewPager.setPageTransformer(true, PageTransform())
        viewPager.currentItem = images.size * 100
        initRadioButton(images.size)
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (isContinue) {
                    handler.sendEmptyMessage(1)
                }
            }
        }, 2000, 2000)
    }

    private fun initRadioButton(length: Int) {
        for (i in 0 until length) {
            val imageView = ImageView(this)
            imageView.setImageResource(R.drawable.rg_selector)
            imageView.setPadding(20, 0, 0, 0)
            radioGroup.addView(imageView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            radioGroup.getChildAt(0).isEnabled = false
        }
    }

    private var onPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            index = position
            setCurrentDot(index % images.size)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun setCurrentDot(i: Int) {
        if (radioGroup.getChildAt(i) != null) {
            //当前按钮不可改变
            radioGroup.getChildAt(i).isEnabled = false
        }
        if (radioGroup.getChildAt(preIndex) != null) {
            //上个按钮可以改变
            radioGroup.getChildAt(preIndex).isEnabled = true
            //当前位置变为上一个，继续下次轮播
            preIndex = i
        }
    }

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    index++
                    viewPager!!.currentItem = index
                }
            }
        }
    }
}