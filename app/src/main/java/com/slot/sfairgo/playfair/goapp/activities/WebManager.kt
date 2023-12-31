package com.slot.sfairgo.playfair.goapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.slot.sfairgo.playfair.goapp.R
import com.slot.sfairgo.playfair.goapp.clients.WebChrumClient
import com.slot.sfairgo.playfair.goapp.clients.WebPageClient
import com.slot.sfairgo.playfair.goapp.forEasierLive.WebPageContainerInitializer
import com.slot.sfairgo.playfair.goapp.forEasierLive.WebPageContainerData
import java.io.File
import java.io.IOException


class WebManager : AppCompatActivity() {
    private var webPageContainer: WebView? = null
    private var firstPartCallback: ValueCallback<Array<Uri>>? = null
    private var secondPartCallback: Uri? = null

    private val callback1: (WebResourceRequest) -> Boolean = {
        it.url.toString().contains("/")
    }
    private val callback2: (WebResourceRequest) -> Boolean = {
        !(it.url.toString().contains("http") && it.url.toString().contains(":"))
    }

    private val callback3: () -> Unit = {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
    private val callback4: (ValueCallback<Array<Uri>>) -> Boolean = {
        firstPartCallback = it
        true
    }

    inner class TempFileCreater() {

        private val prefix = "temp_picture_file"
        private val suffix = ".jpg"
        fun createTempFile(): File? {
            return File.createTempFile(
                prefix,
                suffix,
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )
        }

        fun createTempFile(someData: String): File? {
            return File.createTempFile(
                prefix,
                suffix
            ).apply {
                writeText(someData)
            }
        }
    }

    private val elseBranch = {
        onReceiveValue(null)
    }

    val badBranch: (Boolean) -> Unit = {
        if (it) {
            onReceiveValue(arrayOf(secondPartCallback!!))
        } else {
            elseBranch()
        }
    }

    private val oIntentFactory: Intent
        get() {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            return intent
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_web)
        webPageContainer = findViewById(R.id.webPageContainer)
        webPageContainer?.let {
            CookieManager.getInstance().setAcceptThirdPartyCookies(it, true)
            doInitialization(it)
            it.loadUrl(WebPageContainerData.destinationCheck.value)
        }
    }

    private fun doInitialization(webPageContainer: WebView) {
        val parameter = WebPageContainerInitializer.SetParameter.Builder()
            .cacheMode(WebSettings.LOAD_DEFAULT)
            .mixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
            .defaultBooleanValue(true)
            .userStringReplacement("; wv", "")
            .build()
        WebPageContainerInitializer.set(parameter, webPageContainer)

        webPageContainer.webChromeClient = WebChrumClient(callback3, callback4)
        webPageContainer.webViewClient = WebPageClient(callback1, callback2)
    }

    private val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { _: Boolean? ->
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).after { tIntent ->
            oIntentFactory.after { oIntent ->
                Intent(Intent.ACTION_CHOOSER).after { cIntent ->
                    var photoFile: File? = null
                    lifecycleScope.launch(Dispatchers.IO) {
                        photoFile = try {
                            TempFileCreater().createTempFile()
                        } catch (ex: IOException) {
                            Log.e("Picture file bad", "Bad: ", ex)
                            null
                        }
                    }.invokeOnCompletion {
                        tIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                        cIntent.apply {
                            putExtra(Intent.EXTRA_INTENT, oIntent)
                            secondPartCallback = Uri.fromFile(photoFile)
                            putExtra(Intent.EXTRA_INITIAL_INTENTS, Array(1) { tIntent })
                        }.after {
                            startActivityForResult(it, 1)
                        }
                    }
                }
            }
        }
    }

    private fun<T> T.after(action: (T) -> Unit) {
        action(this)
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val cond1 = firstPartCallback == null
        val cond2 = secondPartCallback == null
        if (!cond1) {
            if (resultCode == -1) {
                data?.dataString?.let {
                    val u = Uri.parse(it)
                    onReceiveValue(arrayOf(u))
                } ?: badBranch(!cond2)
            } else {
                elseBranch()
            }
        }
    }

    private fun onReceiveValue(array: Array<Uri>?) {
        firstPartCallback?.onReceiveValue(array)
        firstPartCallback = null
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webPageContainer?.saveState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        fun goIfCan(): Boolean {
            val cond = webPageContainer?.canGoBack() == true
            if(cond) {
                webPageContainer?.goBack()
            }
            return cond
        }
        return keyCode == 4 && goIfCan()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webPageContainer?.restoreState(savedInstanceState)
    }
}