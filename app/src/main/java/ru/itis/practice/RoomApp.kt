package ru.itis.practice

import android.app.Application
import android.graphics.Bitmap
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkerFactory
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.itis.practice.data.session.SessionManager
import ru.itis.practice.presentation.startup.AppStartUpManager
import javax.inject.Inject


@HiltAndroidApp
class RoomApp: Application(), Configuration.Provider {
    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var appStartUpManager: AppStartUpManager

    override fun onCreate() {
        super.onCreate()
        MainScope().launch {
            sessionManager.initializeSession()
            delay(3000)
            appStartUpManager.startDeleteOldUsersWork()

        }
    }
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}