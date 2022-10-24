package com.donfyy.skin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.donfyy.skin.fragment.MusicFragment
import com.donfyy.skin.fragment.RadioFragment
import com.donfyy.skin.fragment.VideoFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val fragments = mutableListOf(MusicFragment(), VideoFragment(), RadioFragment())
        val fragmentTitles = mutableListOf("音乐", "视频", "电台");
        viewPager.adapter =
            MyFragmentPagerAdapter(supportFragmentManager, fragments, fragmentTitles)
        tabLayout.setupWithViewPager(viewPager)
    }

    fun skinSelect(view: View) {
        startActivity(Intent(this, SkinActivity::class.java))
    }
}
