package one.two.fairgo6.views.subordinates.roulette

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class RouletteTools: ViewModel() {
    val betToLastWin = MutableStateFlow(10) to MutableStateFlow(0)
    val balanceToJackpot = MutableStateFlow(10000) to MutableStateFlow(0)
}