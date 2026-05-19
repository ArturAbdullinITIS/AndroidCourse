package com.example.hw6.util

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsInit {
    fun init(context: Context) {
        val userId = UserIdProvider.getOrCreate(context)

        FirebaseCrashlytics.getInstance().setUserId(userId)
        FirebaseCrashlytics.getInstance().setCustomKey("user_id", userId)
    }

    fun logScreen(screen: String) {
        FirebaseCrashlytics.getInstance().log("screen=$screen")
    }

    fun logDetails(bookId: String) {
        FirebaseCrashlytics.getInstance().log("screen=Details bookId=$bookId")
    }
}