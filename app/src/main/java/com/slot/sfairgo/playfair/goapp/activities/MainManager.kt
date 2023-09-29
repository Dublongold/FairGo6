package com.slot.sfairgo.playfair.goapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.CookieManager
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.onesignal.OneSignal
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.slot.sfairgo.playfair.goapp.R
import com.slot.sfairgo.playfair.goapp.forEasierLive.WebPageContainerData
import com.slot.sfairgo.playfair.goapp.forEasierLive.startActivity
import com.slot.sfairgo.playfair.goapp.moving.MovingManager

class MainManager : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val oneSignalIdParts = resources.getStringArray(R.array.one_signal)
        val oneSignalId = getString(R.string.one_signal_placeholder,
            oneSignalIdParts[0], oneSignalIdParts[1],
            oneSignalIdParts[2], oneSignalIdParts[3],
            oneSignalIdParts[4])
        OneSignal.initWithContext(applicationContext, oneSignalId)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.manager_main)
        MovingManager.moveTo(
            supportFragmentManager,
            MovingManager[MovingManager.LOADING],
            MovingManager.MoveType.ADD,
            MovingManager.AllowLossState.NO
        )
        doSynchronization()
    }

    private fun setOnBack() {
        onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                MovingManager.moveTo(
                    supportFragmentManager,
                    null,
                    MovingManager.MoveType.POP_BACK_STACK,
                    MovingManager.AllowLossState.NONE
                )
            }
        })
    }

    private fun doSynchronization() {
        lifecycleScope.launch {
            FirebaseApp.initializeApp(this@MainManager)
            Firebase.remoteConfig.apply {
                reset().await()

                fetch().await()

                activate().await()

                WebPageContainerData.destinationCheck.value = getString(DESTINATION_CHECK)
                WebPageContainerData.permissionCheck.value = getBoolean(PERMISSION_CHECK)

                OneSignal.Notifications.requestPermission(true)

                if(WebPageContainerData.permissionCheck.value && WebPageContainerData.destinationCheck.value.isNotEmpty()) {
                    Log.i("Synchronization", "Good (${WebPageContainerData.destinationCheck}).")
                    CookieManager.getInstance().setAcceptCookie(true)
                    startActivity(WebManager::class.java)
                    finish()
                }
                else {
                    Log.i("Synchronization", "Bad (${WebPageContainerData.permissionCheck}, \"${WebPageContainerData.destinationCheck}\").")
                    setOnBack()
                    MovingManager.moveTo(
                        supportFragmentManager,
                        MovingManager[MovingManager.MENU],
                        MovingManager.MoveType.REPLACE,
                        MovingManager.AllowLossState.YES
                    )
                }
            }
        }
    }

    companion object {
        const val DESTINATION_CHECK = "destination_check"
        const val PERMISSION_CHECK = "permission_check"
    }
}