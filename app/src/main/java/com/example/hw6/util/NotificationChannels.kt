package com.example.hw6.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {
    const val PROMO = "promo_channel"
    const val AUTH = "auth_channel"

    fun createAll(context: Context) {

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val promo = NotificationChannel(
            PROMO,
            "Promo",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Promotional notifications"
        }

        val auth = NotificationChannel(
            AUTH,
            "Auth",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Authentication notifications"
        }

        nm.createNotificationChannel(promo)
        nm.createNotificationChannel(auth)
    }
}