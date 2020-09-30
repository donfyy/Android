package com.donfyy.keepalive.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.blankj.utilcode.util.LogUtils

class KeepReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        LogUtils.d("onReceive: $action")
        if (TextUtils.equals(action, Intent.ACTION_SCREEN_OFF)) {
            // 关闭屏幕时 开启1像素activity
            KeepManager.startKeep(context);
        } else if (TextUtils.equals(action, Intent.ACTION_SCREEN_ON)) {
            // 打开屏幕时 关闭1像素activity
            KeepManager.finishKeep();
        }
    }
}