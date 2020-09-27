package com.donfyy.viewpager.lazyloading.lazy4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.donfyy.viewpager.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class LazyMain4Activity : AppCompatActivity() {
    private lateinit var fragmentsList: ArrayList<androidx.fragment.app.Fragment>
    private lateinit var bottomNavigationView: BottomNavigationView
    val fragmentMap = hashMapOf<Int, Fragment>()
    val fragmentClassMap = hashMapOf<Int, Class<out Fragment>>(
            R.id.fragment_1 to Fragment1::class.java,
            R.id.fragment_2 to Fragment2WithViewPager::class.java,
            R.id.fragment_3 to Fragment3::class.java,
            R.id.fragment_4 to Fragment4::class.java,
            R.id.fragment_5 to Fragment5::class.java
    )
    var currentItemId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_lazy_4)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { show(it.itemId) }
        show(R.id.fragment_1)
    }

    private fun show(newItemId: Int): Boolean {
        if (newItemId == currentItemId) return false
        val oldItemId = currentItemId
        supportFragmentManager.beginTransaction().apply {
            val sb = StringBuilder()
            fragmentMap[oldItemId]?.let {
                hide(it)
                sb.append("hide ${it.javaClass.simpleName} ")
            }
            var f = fragmentMap[newItemId]
            if (f == null) {
                f = fragmentClassMap[newItemId]!!.newInstance()
                fragmentMap[newItemId] = f
                add(R.id.content, f)
                sb.append("add ${f.javaClass.simpleName} ")
            } else {
                show(f)
                sb.append("show ${f.javaClass.simpleName} ")
            }
            commitAllowingStateLoss()
            LogUtils.d(sb.toString())
        }
        currentItemId = newItemId
        return true
    }
}