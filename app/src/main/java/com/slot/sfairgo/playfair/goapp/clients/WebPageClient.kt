package com.slot.sfairgo.playfair.goapp.clients

import android.util.Log
import android.view.KeyEvent
import android.webkit.ClientCertRequest
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.gms.common.api.Api.Client

class WebPageClient(
    private val callback1: (WebResourceRequest) -> Boolean,
    private val callback2: (WebResourceRequest) -> Boolean
): WebViewClient() {
    private var onReceivedClientCertRequest: ClientCertRequest? = null
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        Log.i("Just for fun", "onReceivedClientCertRequest: $onReceivedClientCertRequest")
        return if (callback1(request)) {
            callback2(request)
        } else true
    }

    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        super.onReceivedClientCertRequest(view, request)
        Log.i("RecCliCertRequest", "view: $view\nrequest: $request")
        onReceivedClientCertRequest = request
    }

    override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
        Log.i("ShouldOverKeyEvent", "No, you shouldn't. Event: $event.")
        return super.shouldOverrideKeyEvent(view, event)
    }
}