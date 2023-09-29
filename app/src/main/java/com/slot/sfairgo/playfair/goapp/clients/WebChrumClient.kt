package com.slot.sfairgo.playfair.goapp.clients

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

class WebChrumClient(
    private val callback1: () -> Unit,
    private val callback2: (ValueCallback<Array<Uri>>) -> Boolean
): WebChromeClient() {
    var requestGot: Boolean? = null
    override fun onShowFileChooser(
        webView: WebView,
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams
    ): Boolean {
        callback1()
        return callback2(filePathCallback)
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        super.onPermissionRequest(request)
        requestGot = true
        Log.w("Permission", "Got.")
    }

    override fun onPermissionRequestCanceled(request: PermissionRequest?) {
        super.onPermissionRequestCanceled(request)
        requestGot = false
        Log.w("Permission", "Cancelled.")
    }

    override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
        super.onReceivedIcon(view, icon)
        Log.i("Received icon", "${icon?.width}x${icon?.height}.")
    }

    override fun getVisitedHistory(callback: ValueCallback<Array<String>>?) {
        super.getVisitedHistory(callback)
        Log.i("Visited history", "Callback is null equals ${callback == null}.")
    }
}