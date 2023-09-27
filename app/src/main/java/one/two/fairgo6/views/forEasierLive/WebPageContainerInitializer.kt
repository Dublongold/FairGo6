package one.two.fairgo6.views.forEasierLive

import android.webkit.WebSettings
import android.webkit.WebView

object WebPageContainerInitializer {
    fun set(parameter: SetParameter, webPageContainer: WebView) {
        webPageContainer.settings.run {
            setBooleans(
                parameter,
                ::setAllowContentAccess,
                ::setAllowFileAccessFromFileURLs,
                ::setJavaScriptCanOpenWindowsAutomatically,
                ::setDomStorageEnabled,
                ::setAllowUniversalAccessFromFileURLs,
            )
            setBooleans(
                parameter,
                ::setAllowFileAccess,
                ::setJavaScriptEnabled,
                ::setLoadWithOverviewMode,
                ::setDatabaseEnabled,
                ::setUseWideViewPort,
            )
            setIntegers(parameter, webPageContainer.settings)
            webPageContainer.settings.userAgentString = webPageContainer.settings.userAgentString
                .replace(parameter.userStringOldChats, parameter.userStringNewChats)
        }
    }
    /*
        webPageContainer!!.settings.allowContentAccess = true //
        webPageContainer!!.settings.allowFileAccess = true
        webPageContainer!!.settings.javaScriptCanOpenWindowsAutomatically = true //
        webPageContainer!!.settings.allowFileAccessFromFileURLs = true //
        webPageContainer!!.settings.mixedContentMode = 0
        webPageContainer!!.settings.cacheMode = WebSettings.LOAD_DEFAULT
        val usrAgent = webPageContainer!!.settings.userAgentString
        webPageContainer!!.settings.setUserAgentString(usrAgent.replace("; wv", ""))
        webPageContainer!!.settings.domStorageEnabled = true //
        webPageContainer!!.settings.javaScriptEnabled = true
        webPageContainer!!.settings.databaseEnabled = true
        webPageContainer!!.settings.allowUniversalAccessFromFileURLs = true //
        webPageContainer!!.settings.useWideViewPort = true
        webPageContainer!!.settings.loadWithOverviewMode = true
     */

    private fun setBooleans(parameter: SetParameter, vararg booleans: (Boolean) -> Unit) {
        for(boolean in booleans) {
            boolean(parameter.defaultBooleanValue)
        }
    }

    private fun setIntegers(parameter: SetParameter, settings: WebSettings) {
        settings.mixedContentMode = parameter.mixedContentMode
        settings.cacheMode = parameter.cacheMode
    }

    class SetParameter {
        var defaultBooleanValue = false
        var userStringOldChats = ""
        var userStringNewChats = ""
        var mixedContentMode = -999
        var cacheMode = -999

        class Builder() {
            private val setParameter = SetParameter()
            fun defaultBooleanValue(value: Boolean): Builder {
                setParameter.defaultBooleanValue = value
                return this
            }

            fun userStringReplacement(oldChars: String, newChars: String): Builder {
                setParameter.userStringOldChats = oldChars
                setParameter.userStringNewChats = newChars
                return this
            }
            fun mixedContentMode(value: Int): Builder {
                setParameter.mixedContentMode = value
                return this
            }
            fun cacheMode(value: Int): Builder {
                setParameter.cacheMode = value
                return this
            }
            fun build(): SetParameter {
                return setParameter
            }
        }
    }
}