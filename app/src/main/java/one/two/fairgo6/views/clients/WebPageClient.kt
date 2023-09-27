package one.two.fairgo6.views.clients

import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class WebPageClient(
    private val callback1: (WebResourceRequest) -> Boolean,
    private val callback2: (WebResourceRequest) -> Boolean
): WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return if (callback1(request)) {
            callback2(request)
        } else true
    }
}