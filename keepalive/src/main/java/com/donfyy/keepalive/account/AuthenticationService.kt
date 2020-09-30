package com.donfyy.keepalive.account

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.NetworkErrorException
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder

/**
 * 创建 可添加用户
 */
class AuthenticationService : Service() {
    private var accountAuthenticator: AccountAuthenticator? = null
    override fun onBind(intent: Intent): IBinder? {
        return accountAuthenticator!!.iBinder
    }

    override fun onCreate() {
        super.onCreate()
        accountAuthenticator = AccountAuthenticator(this)
    }

    class AccountAuthenticator(context: Context?) : AbstractAccountAuthenticator(context) {
        override fun editProperties(response: AccountAuthenticatorResponse, accountType: String): Bundle? {
            return null
        }

        @Throws(NetworkErrorException::class)
        override fun addAccount(response: AccountAuthenticatorResponse, accountType: String, authTokenType: String,
                                requiredFeatures: Array<String>, options: Bundle): Bundle? {
            return null
        }

        @Throws(NetworkErrorException::class)
        override fun confirmCredentials(response: AccountAuthenticatorResponse, account: Account,
                                        options: Bundle): Bundle ?{
            return null
        }

        @Throws(NetworkErrorException::class)
        override fun getAuthToken(response: AccountAuthenticatorResponse, account: Account,
                                  authTokenType: String, options: Bundle): Bundle ?{
            return null
        }

        override fun getAuthTokenLabel(authTokenType: String): String? {
            return null
        }

        @Throws(NetworkErrorException::class)
        override fun updateCredentials(response: AccountAuthenticatorResponse, account: Account,
                                       authTokenType: String, options: Bundle): Bundle? {
            return null
        }

        @Throws(NetworkErrorException::class)
        override fun hasFeatures(response: AccountAuthenticatorResponse, account: Account,
                                 features: Array<String>): Bundle? {
            return null
        }
    }
}