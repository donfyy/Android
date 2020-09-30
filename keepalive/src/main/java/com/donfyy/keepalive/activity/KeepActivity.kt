package com.donfyy.keepalive.activity

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import com.blankj.utilcode.util.LogUtils
import com.donfyy.keepalive.activity.KeepManager.setKeep

class KeepActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d("启动keep")
        val window = window
        //放在左上角
        window.setGravity(Gravity.START or Gravity.TOP)
        val params = window.attributes
        //设置宽高
        params.width = 1
        params.height = 1
        //设置起始坐标
        params.x = 0
        params.y = 0
        window.attributes = params

        // KeepActivity 创建一个弱引用
        setKeep(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d("关闭keep")
    }

}