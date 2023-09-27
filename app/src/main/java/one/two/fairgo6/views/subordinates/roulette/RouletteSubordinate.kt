package one.two.fairgo6.views.subordinates.roulette

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import one.two.fairgo6.R
import one.two.fairgo6.views.forEasierLive.FromRotationToValue
import one.two.fairgo6.views.moving.MovingManager

class RouletteSubordinate: Fragment() {

    private val tools: RouletteTools by viewModels()
    private lateinit var sectors: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.subordinate_roulette, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

        view.setBackgroundResource(R.drawable.background_3)

        val arialFont = ResourcesCompat.getFont(requireContext(), R.font.arial_bold)

        val balanceInfo = TextView(context)
        tools.balanceToJackpot.first.observe {

        }
        tools.balanceToJackpot.first.observe {
            balanceInfo.text = getString(R.string.game_1, it)
        }
        balanceInfo.typeface = arialFont
        balanceInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        balanceInfo.isAllCaps = true
        balanceInfo.gravity = Gravity.CENTER
        balanceInfo.setBackgroundResource(R.drawable.default_text_bg)

        var layoutParams = LinearLayout.LayoutParams(
            200.dp(),
            48.dp()
        )
        layoutParams.setMargins(0, 16.dp(), 0, 0)
        balanceInfo.layoutParams = layoutParams

        val lastWinInfo = TextView(context)
        tools.betToLastWin.second.observe {
            lastWinInfo.text = getString(R.string.game_2, it)
        }
        lastWinInfo.typeface = arialFont
        lastWinInfo.isAllCaps = true
        lastWinInfo.gravity = Gravity.CENTER
        lastWinInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        lastWinInfo.setBackgroundResource(R.drawable.default_text_bg)

        layoutParams = LinearLayout.LayoutParams(
            200.dp(),
            48.dp()
        )
        layoutParams.setMargins(0, 8.dp(), 0, 0)
        lastWinInfo.layoutParams = layoutParams

        val betInfo = TextView(context)
        betInfo.typeface = arialFont
        betInfo.isAllCaps = true
        betInfo.gravity = Gravity.CENTER
        betInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

        layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.weight = 1f
        betInfo.layoutParams = layoutParams

        val jackpotInfo = TextView(context)
        tools.betToLastWin.first.observe {
            betInfo.text = getString(R.string.game_3, it)
            tools.balanceToJackpot.second.value = it * 40
        }
        tools.balanceToJackpot.second.observe {
            jackpotInfo.text = getString(R.string.game_4, it)
        }
        jackpotInfo.typeface = arialFont
        jackpotInfo.isAllCaps = true
        jackpotInfo.gravity = Gravity.CENTER
        jackpotInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        jackpotInfo.setBackgroundResource(R.drawable.default_text_bg)

        layoutParams = LinearLayout.LayoutParams(
            143.dp(),
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.setMargins(9.dp(), 0, 0, 0)
        jackpotInfo.layoutParams = layoutParams

        val roulette = createRoulette()
        sectors = roulette[1]

        view as ViewGroup

        view.findViewById<LinearLayout>(R.id.roulette_container_1).apply {
            addView(balanceInfo, 1)
            addView(lastWinInfo, 2)
        }
        view.findViewById<LinearLayout>(R.id.roulette_container_2).addView(jackpotInfo, 1)
        view.findViewById<LinearLayout>(R.id.roulette_container_3).addView(betInfo, 1)
        for(roll in roulette) {
            view.addView(roll)
        }
        view.findViewById<ImageButton>(R.id.roulette_button_1).setOnClickListener {
            MovingManager.moveTo(
                parentFragmentManager,
                null,
                MovingManager.MoveType.POP_BACK_STACK,
                MovingManager.AllowLossState.NONE
            )
        }

        view.findViewById<ImageButton>(R.id.roulette_button_2).setOnClickListener {
            if(tools.betToLastWin.first.value > 10) {
                tools.betToLastWin.first.value -= 10
            }
        }
        view.findViewById<ImageButton>(R.id.roulette_button_3).setOnClickListener {
            tools.betToLastWin.first.value += 10
        }
        view.findViewById<ImageButton>(R.id.roulette_button_4).setOnClickListener {
            it.isEnabled = false
            roll {
                it.isEnabled = true
            }
        }
    }

    private fun<T> MutableStateFlow<T>.observe(action: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect(action)
            }
        }
    }

    private fun createRoulette(): List<ImageView> {
        val roulette1 = ImageView(context)
        roulette1.id = R.id.roulette_1
        roulette1.setImageResource(R.drawable.roulette_1)
        var layoutParams = ConstraintLayout.LayoutParams(
            280.dp(), 280.dp()
        )
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.startToEnd = R.id.roulette_container_1
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

        roulette1.layoutParams = layoutParams

        val roulette2 = ImageView(context)
        roulette2.setImageResource(R.drawable.roulette_2)
        layoutParams = ConstraintLayout.LayoutParams(
            239.75.dp(), 238.88.dp()
        )
        layoutParams.bottomToBottom = R.id.roulette_1
        layoutParams.startToStart = R.id.roulette_1
        layoutParams.topToTop = R.id.roulette_1
        layoutParams.endToEnd = R.id.roulette_1
        roulette2.layoutParams = layoutParams

        val roulette3 = ImageView(context)
        roulette3.setImageResource(R.drawable.roulette_3)
        layoutParams = ConstraintLayout.LayoutParams(
            21.dp(), 35.dp()
        )
        layoutParams.startToStart = R.id.roulette_1
        layoutParams.topToTop = R.id.roulette_1
        layoutParams.endToEnd = R.id.roulette_1
        layoutParams.setMargins(0, 5.25.dp(), 0, 0)

        roulette3.layoutParams = layoutParams

        val roulette4 = ImageView(context)
        roulette4.setImageResource(R.drawable.roulette_4)
        layoutParams = ConstraintLayout.LayoutParams(
            48.dp(), 48.dp()
        )
        layoutParams.bottomToBottom = R.id.roulette_1
        layoutParams.startToStart = R.id.roulette_1
        layoutParams.topToTop = R.id.roulette_1
        layoutParams.endToEnd = R.id.roulette_1
        roulette4.layoutParams = layoutParams

        return listOf(
            roulette1, roulette2, roulette3, roulette4
        )
    }

    private fun roll(callback: () -> Unit) {
        if(tools.balanceToJackpot.first.value - tools.betToLastWin.first.value >= 0) {
            tools.balanceToJackpot.first.value -= tools.betToLastWin.first.value
            tools.betToLastWin.first.value.let { bet ->
                viewLifecycleOwner.lifecycleScope.launch {
                    tools.betToLastWin.second.value = 0
                    for (i in 1..10) {
                        repeat(2) {
                            sectorsRotation += i
                            delay(50)
                        }
                    }
                    var rouletteState = RouletteState.ROLL
                    launch {
                        delay(
                            (3000 + listOf(500, 750, 1000, 1250, 1500, 1750, 2000).random())
                                .toLong()
                        )
                        rouletteState = RouletteState.STOP
                    }
                    while (rouletteState == RouletteState.ROLL) {
                        sectorsRotation += 10
                        delay(50)
                    }
                    for (i in 10 downTo 1) {
                        repeat(2) {
                            sectorsRotation += i
                            delay(50)
                        }
                    }
                    while (FromRotationToValue[sectorsRotation] == -1f) {
                        sectorsRotation += 1
                        delay(50)
                    }
                    tools.betToLastWin.second.value =
                        (bet * FromRotationToValue[sectorsRotation]).toInt()

                    tools.balanceToJackpot.first.value += tools.betToLastWin.second.value
                    callback()
                }
            }
        }
        else {
            callback()
        }
    }

    enum class RouletteState {
        ROLL,
        STOP,
    }

    private var sectorsRotation
        get() = sectors.rotation
        set(value) {
            sectors.rotation = if(value > 360) {
                value - 360
            }
            else {
                value
            }
        }

    private fun Int.dp() = (resources.displayMetrics.density * this).toInt()
    private fun Double.dp() = (resources.displayMetrics.density * this).toInt()
}