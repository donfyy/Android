package com.donfyy.keepalive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.donfyy.keepalive.jobscheduler.KeepJobService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 一像素保活
//        KeepManager.registerKeep(this)
        // 前台服务保活
//        startService(Intent(this, ForegroundService::class.java))
        // 账户同步拉活
//        AccountHelper.addAccount(this)
//        AccountHelper.autoSync()
        // Job Scheduler 拉活
        KeepJobService.startJob(this)
    }

    override fun onDestroy() {
//        KeepManager.unregisterKeep(this)
        super.onDestroy()
    }
}
