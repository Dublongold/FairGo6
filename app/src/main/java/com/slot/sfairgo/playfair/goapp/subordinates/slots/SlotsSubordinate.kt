package com.slot.sfairgo.playfair.goapp.subordinates.slots

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
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.slot.sfairgo.playfair.goapp.R
import com.slot.sfairgo.playfair.goapp.moving.MovingManager

class SlotsSubordinate: Fragment() {

    private val tools: SlotsTools by viewModels()
    private lateinit var elements: List<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.subordinate_slots, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

        view.setBackgroundResource(R.drawable.background_3)

        val arialFont = ResourcesCompat.getFont(requireContext(), R.font.arial_bold)

        val balanceInfo = TextView(context)
        tools.balanceToAuto.first.observe {
            balanceInfo.text = getString(R.string.game_1, it)
        }
        balanceInfo.typeface = arialFont
        balanceInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        balanceInfo.isAllCaps = true
        balanceInfo.gravity = Gravity.CENTER
        balanceInfo.setBackgroundResource(R.drawable.default_text_bg)

        var layoutParams = LinearLayout.LayoutParams(
            244.dp(),
            48.dp()
        )
        layoutParams.setMargins(16.dp(), 0, 16.dp(), 0)
        balanceInfo.layoutParams = layoutParams

        val lastWinInfo = TextView(context)
        lastWinInfo.id = R.id.slots_info_1
        tools.betToLastWin.second.observe {
            lastWinInfo.text = getString(R.string.game_2, it)
        }
        lastWinInfo.typeface = arialFont
        lastWinInfo.isAllCaps = true
        lastWinInfo.gravity = Gravity.CENTER
        lastWinInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        lastWinInfo.setBackgroundResource(R.drawable.default_text_bg)

        layoutParams = LinearLayout.LayoutParams(
            244.dp(),
            48.dp()
        )
        lastWinInfo.layoutParams = layoutParams

        val betInfo = TextView(context)
        tools.betToLastWin.first.observe {
            betInfo.text = getString(R.string.game_3, it)
        }
        betInfo.typeface = arialFont
        betInfo.isAllCaps = true
        betInfo.gravity = Gravity.CENTER
        betInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

        val linearLayoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        linearLayoutParams.weight = 1f
        betInfo.layoutParams = linearLayoutParams

        val slots = ImageView(context)

        val constraintlayoutParams = ConstraintLayout.LayoutParams(
            379.dp(),
            185.dp()
        )

        constraintlayoutParams.topToBottom = R.id.slots_container_0
        constraintlayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        constraintlayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        constraintlayoutParams.bottomToTop = R.id.slots_container_1


        slots.layoutParams = constraintlayoutParams
        slots.id = R.id.slots
        slots.setImageResource(R.drawable.slots)

        view as ViewGroup

        fun createElement(id: Int): ImageView {
            val element = ImageView(context)
            element.setImageResource(R.drawable.slot_1 + id)
            val tempLayoutParams = ConstraintLayout.LayoutParams(
                80.dp(), 80.dp()
            )
            if(id == 1) {
                element.id = R.id.slots_element_1
            }
            tempLayoutParams.topToTop = R.id.slots
            if(id != 0) {
                tempLayoutParams.endToEnd = R.id.slots
            }
            else {
                tempLayoutParams.endToStart = R.id.slots_element_1
            }
            if(id != 2) {
                tempLayoutParams.startToStart = R.id.slots
            }
            else {
                tempLayoutParams.startToEnd = R.id.slots_element_1
            }
            tempLayoutParams.bottomToBottom = R.id.slots
            element.layoutParams = tempLayoutParams
            element.tag = id
            return element
        }

        elements = listOf(
            createElement(0),
            createElement(1),
            createElement(2),
        )

        view.findViewById<LinearLayout>(R.id.slots_container_0).apply {
            addView(balanceInfo)
            addView(lastWinInfo)
        }
        view.findViewById<LinearLayout>(R.id.slots_container_2).addView(betInfo, 1)
        view.addView(slots)
        for(element in elements) {
            view.addView(element)
        }
        view.findViewById<ImageButton>(R.id.slots_button_1).setOnClickListener {
            MovingManager.moveTo(
                parentFragmentManager,
                null,
                MovingManager.MoveType.POP_BACK_STACK,
                MovingManager.AllowLossState.NONE
            )
        }

        view.findViewById<ImageButton>(R.id.slots_button_2).setOnClickListener {
            if(tools.betToLastWin.first.value > 10) {
                tools.betToLastWin.first.value -= 10
            }
        }
        view.findViewById<ImageButton>(R.id.slots_button_3).setOnClickListener {
            tools.betToLastWin.first.value += 10
        }
        view.findViewById<AppCompatButton>(R.id.slots_button_6).setOnClickListener {
            it.isEnabled = false
            roll {
                it.isEnabled = true
            }
        }
        val auto = view.findViewById<AppCompatButton>(R.id.slots_button_4)
        val stop = view.findViewById<AppCompatButton>(R.id.slots_button_5)
        tools.balanceToAuto.second.observe {
            auto.isEnabled = !it
            auto.setTextColor(resources.getColor(if(!it) R.color.color_2 else R.color.white, null))
            stop.isEnabled = it
            stop.setTextColor(resources.getColor(if(it) R.color.color_2 else R.color.white, null))
            if(it) {
                view.findViewById<AppCompatButton>(R.id.slots_button_6).performClick()
            }
        }
        auto.setOnClickListener {
            tools.balanceToAuto.second.value = true
        }
        stop.setOnClickListener {
            tools.balanceToAuto.second.value = false
        }
    }

    private fun roll(callback: () -> Unit) {
        if(tools.balanceToAuto.first.value - tools.betToLastWin.first.value >= 0) {
            tools.balanceToAuto.first.value -= tools.betToLastWin.first.value
            tools.betToLastWin.first.value.let { bet ->
                viewLifecycleOwner.lifecycleScope.launch {
                    tools.betToLastWin.second.value = 0
                    val possibleValues = listOf(0, 1, 2, 3, 4, 5)
                    val jobs = mutableListOf<Job>()
                    for(i in 0..2) {
                        jobs.add(launch {
                            while (true) {
                                elements[i].tag = possibleValues.random()
                                elements[i].setImageResource(R.drawable.slot_1 + elements[i].tag as Int)
                                delay(50)
                            }
                        })
                    }
                    delay((3000 + listOf(250, 500, 750, 1000, 1250, 1500, 1750, 2000).random()).toLong())
                    var cancelled = 0
                    for(job in jobs) {
                        cancelled += 1
                        job.cancel()
                        if(cancelled < 3) {
                            delay(500)
                        }
                    }
                    if(elements.all {
                        it.tag == elements[0].tag
                        }) {
                        tools.betToLastWin.second.value = bet * (elements[1].tag as Int + 1)
                    }
                    callback()
                    if(tools.balanceToAuto.second.value) {
                        view?.findViewById<AppCompatButton>(R.id.slots_button_6)?.performClick()
                    }
                }
            }
        }
        else {
            callback()
        }
    }

    private fun<T> MutableStateFlow<T>.observe(action: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect(action)
            }
        }
    }

    private fun Int.dp() = (resources.displayMetrics.density * this).toInt()
}