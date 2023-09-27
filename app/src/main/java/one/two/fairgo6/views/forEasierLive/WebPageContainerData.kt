package one.two.fairgo6.views.forEasierLive

import kotlinx.coroutines.flow.MutableStateFlow

object WebPageContainerData {
    var url = MutableStateFlow("")
    var allow = MutableStateFlow(false)
}