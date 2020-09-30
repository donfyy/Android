package com.donfyy.keepalive.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.LogUtils

class ForegroundService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.d("ForegroundService 服务创建了")
        when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 -> { // 4.3以下
                // 将service设置成前台服务，并且不显示通知栏消息
                startForeground(SERVICE_ID, Notification())
            }
            Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> { // [Android4.3, Android7.0]
                // 将service设置成前台服务
                startForeground(SERVICE_ID, Notification())
                // 删除通知栏消息
                startService(Intent(this, InnerService::class.java))
            }
            else -> { // 8.0 及以上
                // 通知栏消息需要设置channel
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                // NotificationManager.IMPORTANCE_MIN 通知栏消息的重要级别  最低，不让弹出
                // IMPORTANCE_MIN 前台时，在阴影区能看到，后台时 阴影区不消失，增加显示 IMPORTANCE_NONE时 一样的提示
                // IMPORTANCE_NONE app在前台没有通知显示，后台时有
                val channel = NotificationChannel("channel", "xx", NotificationManager.IMPORTANCE_NONE)
                manager.createNotificationChannel(channel)
                val notification = NotificationCompat.Builder(this, "channel").build()
                // 将service设置成前台服务，8.x退到后台会显示通知栏消息，9.0会立刻显示通知栏消息
                startForeground(SERVICE_ID, notification)
            }
        }
    }

    class InnerService : Service() {
        override fun onCreate() {
            super.onCreate()
            LogUtils.d("InnerService 服务创建了")
            // 让服务变成前台服务
            startForeground(SERVICE_ID, Notification())
            // 关闭自己
            stopSelf()
        }

        override fun onBind(intent: Intent): IBinder? {
            return null
        }
    }

    companion object {
        private const val SERVICE_ID = 1
    }
}