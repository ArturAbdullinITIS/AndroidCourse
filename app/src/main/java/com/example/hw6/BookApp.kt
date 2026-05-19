package com.example.hw6

import android.app.Application
import com.example.hw6.util.CrashlyticsInit
import com.example.hw6.util.NotificationChannels
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BookApp: Application() {
    override fun onCreate() {
        super.onCreate()

        CrashlyticsInit.init(this)

        NotificationChannels.createAll(this)
    }
}