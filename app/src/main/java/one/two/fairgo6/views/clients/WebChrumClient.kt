package one.two.fairgo6.views.clients

import android.Manifest
import android.net.Uri
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class WebChrumClient(
    private val callback1: () -> Unit,
    private val callback2: (ValueCallback<Array<Uri>>) -> Boolean
): WebChromeClient() {
    override fun onShowFileChooser(
        webView: WebView,
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams
    ): Boolean {
        callback1()
        return callback2(filePathCallback)
    }
}