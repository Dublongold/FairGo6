package com.slot.sfairgo.playfair.goapp.subordinates.loading

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.slot.sfairgo.playfair.goapp.R

class LoadingSubordinate: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.subordinate_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.background_1)
        val loadingText = TextView(context)
        loadingText.text = getString(R.string.loading)
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, 50.dp())
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        loadingText.layoutParams = layoutParams
        loadingText.typeface = ResourcesCompat.getFont(requireContext(), R.font.arial_bold)
        loadingText.isAllCaps = true
        loadingText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

        view as ViewGroup

        view.addView(loadingText)
    }

    private fun Int.dp() = (resources.displayMetrics.density * this).toInt()
}