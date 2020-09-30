package com.donfyy.keepalive.account

import android.accounts.Account
import android.app.Service
import android.content.*
import android.os.Bundle
import android.os.IBinder
import com.blankj.utilcode.util.LogUtils

class SyncService : Service() {
    private lateinit var mSyncAdapter: SyncAdapter
    override fun onBind(intent: Intent): IBinder? {
        return mSyncAdapter.syncAdapterBinder
    }

    override fun onCreate() {
        super.onCreate()
        mSyncAdapter = SyncAdapter(applicationContext, true)
    }

    class SyncAdapter(context: Context?, autoInitialize: Boolean) : AbstractThreadedSyncAdapter(context, autoInitialize) {
        override fun onPerformSync(account: Account, extras: Bundle, authority: String, provider: ContentProviderClient, syncResult: SyncResult) {
            LogUtils.d("同步账户")
            //与互联网 或者 本地数据库同步账户
        }
    }
}