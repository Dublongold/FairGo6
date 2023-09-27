package one.two.fairgo6.views.activities

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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import one.two.fairgo6.R
import one.two.fairgo6.views.forEasierLive.WebPageContainerData
import one.two.fairgo6.views.forEasierLive.startActivity
import one.two.fairgo6.views.moving.MovingManager

class MainManager : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

                WebPageContainerData.url.value = getString("url")
                WebPageContainerData.allow.value = getBoolean("allow")
                if(WebPageContainerData.allow.value && WebPageContainerData.url.value.isNotEmpty()) {
                    Log.i("Synchronization", "Good (${WebPageContainerData.url}).")
                    CookieManager.getInstance().setAcceptCookie(true)
                    startActivity(WebManager::class.java)
                    finish()
                }
                else {
                    Log.i("Synchronization", "Bad (${WebPageContainerData.allow}, \"${WebPageContainerData.url}\").")
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
}