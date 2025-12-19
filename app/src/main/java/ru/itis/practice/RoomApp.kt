package ru.itis.practice

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.itis.practice.data.session.SessionManager
import javax.inject.Inject


@HiltAndroidApp
class RoomApp: Application() {
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate() {
        super.onCreate()
        MainScope().launch {
            sessionManager.initializeSession()
        }
    }
}