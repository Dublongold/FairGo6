package com.slot.sfairgo.playfair.goapp.subordinates.privacyPolicy

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.slot.sfairgo.playfair.goapp.R
import com.slot.sfairgo.playfair.goapp.moving.MovingManager

class PrivacyPolicySubordinate: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.subordinate_privacy_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

        view.setBackgroundResource(R.drawable.background_3)

        val titleText = TextView(context)
        titleText.text = getString(R.string.privacy_policy_1)
        titleText.typeface = ResourcesCompat.getFont(requireContext(), R.font.arial_bold)
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        titleText.isAllCaps = true
        titleText.setBackgroundResource(R.drawable.default_text_bg)
        titleText.gravity = Gravity.CENTER
        var layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            48.dp()
        )
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.setMargins(0, 48.dp(), 0, 0)

        titleText.layoutParams = layoutParams
        titleText.setPadding(55.dp(), 5.dp(), 55.dp(), 5.dp())

        val textScroll = ScrollView(context)

        textScroll.setBackgroundResource(R.drawable.privacy_policy_text_bg)

        layoutParams = ConstraintLayout.LayoutParams(
            0,
            0
        )
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.topToBottom = R.id.privacy_policy_button_1
        layoutParams.setMargins(40.dp(), 16.dp(), 40.dp(), 30.dp())

        textScroll.layoutParams = layoutParams
        textScroll.setPadding(16.dp())


        val text = TextView(context)
        text.text = getString(R.string.privacy_policy_2)
        text.typeface = ResourcesCompat.getFont(requireContext(), R.font.arial_bold)
        text.isAllCaps = true
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

        textScroll.addView(text)

        view as ViewGroup

        view.addView(titleText)
        view.addView(textScroll)

        view.findViewById<ImageButton>(R.id.privacy_policy_button_1).setOnClickListener {
            MovingManager.moveTo(
                parentFragmentManager,
                null,
                MovingManager.MoveType.POP_BACK_STACK,
                MovingManager.AllowLossState.NONE
            )
        }
    }

    private fun Int.dp() = (resources.displayMetrics.density * this).toInt()
}