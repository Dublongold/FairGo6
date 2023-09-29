package com.slot.sfairgo.playfair.goapp.forEasierLive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

fun<T> AppCompatActivity.startActivity(clazz: Class<T>) {
    val intent = Intent(this, clazz)
    startActivity(intent)
}