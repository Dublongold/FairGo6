package one.two.fairgo6.views.forEasierLive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

fun<T> AppCompatActivity.startActivity(clazz: Class<T>) {
    val intent = Intent(this, clazz)
    startActivity(intent)
}