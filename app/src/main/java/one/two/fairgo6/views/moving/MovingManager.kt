package one.two.fairgo6.views.moving

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import one.two.fairgo6.R
import one.two.fairgo6.views.subordinates.loading.LoadingSubordinate
import one.two.fairgo6.views.subordinates.menu.MenuSubordinate
import one.two.fairgo6.views.subordinates.privacyPolicy.PrivacyPolicySubordinate
import one.two.fairgo6.views.subordinates.roulette.RouletteSubordinate
import one.two.fairgo6.views.subordinates.slots.SlotsSubordinate

object MovingManager {
    const val LOADING = 0
    const val MENU = 1
    const val SLOTS = 2
    const val ROULETTE = 3
    const val PRIVACY_POLICY = 4

    private val CONTAINER_ID = R.id.manager_main

    fun moveTo(fragmentManager: FragmentManager, fragment: Fragment?, moveType: MoveType, allowLossState: AllowLossState) {
        if(moveType != MoveType.POP_BACK_STACK && fragment != null) {
            fragmentManager.beginTransaction().apply {
                if(moveType == MoveType.ADD) {
                    add(CONTAINER_ID, fragment)
                }
                else {
                    replace(CONTAINER_ID, fragment)
                }
                if(allowLossState == AllowLossState.YES) {
                    commitAllowingStateLoss()
                }
                else {
                    commit()
                }
            }
        }
        else {
            moveOut(fragmentManager)
        }
    }

    private fun moveOut(fragmentManager: FragmentManager) {
        if(fragmentManager.fragments.single() !is MenuSubordinate) {
            fragmentManager.beginTransaction().apply {
                replace(CONTAINER_ID, MovingManager[MENU])
                commitAllowingStateLoss()
            }
        }
        else {
            fragmentManager.fragments.single().activity?.finish()
        }
    }

    operator fun get(fragmentId: Int) = when(fragmentId) {
        LOADING -> LoadingSubordinate()
        MENU -> MenuSubordinate()
        SLOTS -> SlotsSubordinate()
        ROULETTE -> RouletteSubordinate()
        PRIVACY_POLICY -> PrivacyPolicySubordinate()
        else -> Fragment()
    }

    enum class AllowLossState {
        YES,
        NO,
        NONE
    }

    enum class MoveType {
        ADD,
        REPLACE,
        POP_BACK_STACK
    }
}