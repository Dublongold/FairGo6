package one.two.fairgo6.views.subordinates.slots

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SlotsTools: ViewModel() {
    val betToLastWin = MutableStateFlow(10) to MutableStateFlow(0)
    val balanceToAuto = MutableStateFlow(10000) to MutableStateFlow(false)
}