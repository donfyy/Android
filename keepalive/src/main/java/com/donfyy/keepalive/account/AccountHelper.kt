package com.donfyy.keepalive.account

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.util.Log

object AccountHelper {
    private const val TAG = "AccountHelper"
    private const val ACCOUNT_TYPE = "com.donfyy.daemon.account"

    /**
     * 添加账号
     *
     * @param context
     */
    fun addAccount(context: Context) {
        val accountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

        // 获得此类型的账户
        // 需要增加权限  GET_ACCOUNTS
        val accounts = accountManager.getAccountsByType(ACCOUNT_TYPE)
        if (accounts.isNotEmpty()) {
            Log.e(TAG, "账户已存在")
            return
        }
        val account = Account("keep", ACCOUNT_TYPE)
        // 给这个账户类型添加一个账户
        // 需要增加权限  AUTHENTICATE_ACCOUNTS
        accountManager.addAccountExplicitly(account, "xx", Bundle())
    }

    /**
     * 设置账户自动同步
     */
    fun autoSync() {
        val account = Account("keep", ACCOUNT_TYPE)

        // 下面三个都需要同一个权限  WRITE_SYNC_SETTINGS

        // 设置同步
        ContentResolver.setIsSyncable(account, "com.donfyy.daemon.provider", 1)

        // 自动同步
        ContentResolver.setSyncAutomatically(account, "com.donfyy.daemon.provider", true)

        // 设置同步周期
        ContentResolver.addPeriodicSync(account, "com.donfyy.daemon.provider", Bundle(), 1)
    }
}