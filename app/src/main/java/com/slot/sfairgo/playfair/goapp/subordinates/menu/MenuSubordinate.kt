package com.slot.sfairgo.playfair.goapp.subordinates.menu

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import com.slot.sfairgo.playfair.goapp.R
import com.slot.sfairgo.playfair.goapp.moving.MovingManager


class MenuSubordinate: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.subordinate_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.background_2)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

        val moves = listOf(
            { _: View ->
                MovingManager.moveTo(
                    parentFragmentManager,
                    MovingManager[MovingManager.SLOTS],
                    MovingManager.MoveType.REPLACE,
                    MovingManager.AllowLossState.YES
                )
            },
            { _: View ->
                MovingManager.moveTo(
                    parentFragmentManager,
                    MovingManager[MovingManager.ROULETTE],
                    MovingManager.MoveType.REPLACE,
                    MovingManager.AllowLossState.YES
                )
            },
            { _: View ->
                MovingManager.moveTo(
                    parentFragmentManager,
                    MovingManager[MovingManager.PRIVACY_POLICY],
                    MovingManager.MoveType.REPLACE,
                    MovingManager.AllowLossState.YES
                )
            }
        )

        view.allViews.filter {
            it is ImageButton
        }.forEachIndexed { index, button ->
            button.setOnClickListener(moves[index])
        }
    }
}