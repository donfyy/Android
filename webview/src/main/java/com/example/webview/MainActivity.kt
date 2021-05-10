package com.example.webview

import android.app.Activity
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.webkit.*
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import java.util.*

class MainActivity : Activity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        CookieManager.getInstance().apply {
            // false 内存中无法保存cookie
            setAcceptCookie(true)
        }
        val webView = WebView(this, null, 0, false).apply {
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            webChromeClient = object : WebChromeClient() {
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    Log.i("hhh", "shouldOverrideUrlLoading request: ${request?.url}")
                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                    Log.i("hhh", "shouldInterceptRequest request: ${request?.url} + thread:${Thread.currentThread().name}")
                    return super.shouldInterceptRequest(view, request)
                }

                override fun onLoadResource(view: WebView?, url: String?) {
                    super.onLoadResource(view, url)
                    Log.i("hhh", "onLoadResource url: $url")
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.i("hhh", "onPageFinished url: $url isPrivateBrowsingEnabled: $isPrivateBrowsingEnabled acceptThirdPartyCookies:${CookieManager.getInstance().acceptThirdPartyCookies(this@apply)}")
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    Log.i("hhh", "onReceivedError url: ${request?.url} errorCode: ${error?.errorCode}  errorDescription:${error?.description}")
                    super.onReceivedError(view, request, error)
                }

                override fun onReceivedHttpAuthRequest(view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) {
                    Log.i("hhh", "onReceivedHttpAuthRequest host: ${host} realm: $realm ")
                    super.onReceivedHttpAuthRequest(view, handler, host, realm)
                }

                override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                    Log.i("hhh", "onReceivedHttpError url: ${request?.url} response: ${toString(errorResponse)}")
                    super.onReceivedHttpError(view, request, errorResponse)
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
                    return super.shouldInterceptRequest(view, url)
                }

                override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                    Log.i("hhh", "onReceivedError errorCode: ${errorCode} failingUrl$failingUrl description: $description")
                    super.onReceivedError(view, errorCode, description, failingUrl)
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    Log.i("hhh", "onPageStarted url$url ")
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    Log.i("hhh", "onPageCommitVisible url$url ")
                    super.onPageCommitVisible(view, url)
                }

                override fun onTooManyRedirects(view: WebView?, cancelMsg: Message?, continueMsg: Message?) {
                    Log.i("hhh", "onTooManyRedirects ucancelMsgrl$cancelMsg continueMsg$continueMsg ")
                    super.onTooManyRedirects(view, cancelMsg, continueMsg)
                }

                override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
                    super.onFormResubmission(view, dontResend, resend)
                }

                override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                    super.doUpdateVisitedHistory(view, url, isReload)
                }

                override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                    Log.i("hhh", "onReceivedSslError ${error?.url}")
                    super.onReceivedSslError(view, handler, error)
                }

                override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
                    super.onReceivedClientCertRequest(view, request)
                }

                override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
                    return super.shouldOverrideKeyEvent(view, event)
                }

                override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
                    super.onUnhandledKeyEvent(view, event)
                }

                override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
                    super.onScaleChanged(view, oldScale, newScale)
                }

                override fun onReceivedLoginRequest(view: WebView?, realm: String?, account: String?, args: String?) {
                    Log.i("hhh", "onReceivedLoginRequest realm:${realm} account$account args:$args" )
                    super.onReceivedLoginRequest(view, realm, account, args)
                }

                override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                    return super.onRenderProcessGone(view, detail)
                }

                override fun onSafeBrowsingHit(view: WebView?, request: WebResourceRequest?, threatType: Int, callback: SafeBrowsingResponse?) {
                    Log.i("hhh", "onSafeBrowsingHit request:${request?.url} " )
                    super.onSafeBrowsingHit(view, request, threatType, callback)
                }
            }

//            loadUrl("https://open.feishu.cn/")
//            loadUrl("https://www.baidu.com/")
            loadUrl("http://10.85.118.171:3001/")
//            loadUrl("file:///android_asset/test1.html")
        }
        findViewById<FrameLayout>(R.id.content).addView(webView)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDestroy() {
        super.onDestroy()
        CookieManager.getInstance().flush()
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun toString(resp: WebResourceResponse?): String{
    if (resp == null) return ""
    return "statusCode: " + resp.statusCode + " reasonPhrase: "+ resp.reasonPhrase  + " data:"+ resp.data
}