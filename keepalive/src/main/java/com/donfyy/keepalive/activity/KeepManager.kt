package com.donfyy.keepalive.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import java.lang.ref.WeakReference

object KeepManager {
    //广播
    private var mKeepReceiver: KeepReceiver? = null

    //弱引用
    private var mKeepActivity: WeakReference<Activity>? = null

    /**
     * 注册 开屏 关屏 广播
     *
     * @param context
     */
    fun registerKeep(context: Context) {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        mKeepReceiver = KeepReceiver()
        context.registerReceiver(mKeepReceiver, filter)
    }

    /**
     * 注销 广播接收者
     *
     * @param context
     */
    fun unregisterKeep(context: Context) {
        if (mKeepReceiver != null) {
            context.unregisterReceiver(mKeepReceiver)
        }
    }

    /**
     * 开启1像素Activity
     *
     * @param context
     */
    fun startKeep(context: Context) {
        val intent = Intent(context, KeepActivity::class.java)
        // 结合 taskAffinity 一起使用 在指定栈中创建这个activity
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    /**
     * 关闭1像素Activity
     */
    fun finishKeep() {
        if (mKeepActivity != null) {
            val activity = mKeepActivity!!.get()
            activity?.finish()
            mKeepActivity = null
        }
    }

    /**
     * 设置弱引用
     *
     * @param keep
     */
    fun setKeep(keep: KeepActivity) {
        mKeepActivity = WeakReference(keep)
    }
}