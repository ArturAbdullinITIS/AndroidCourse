package com.example.hw6.util

import android.content.Context
import java.util.UUID
import androidx.core.content.edit

object UserIdProvider {
    private const val PREFS = "app_prefs"
    private const val KEY_USER_ID = "user_id"

    fun getOrCreate(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val existing = prefs.getString(KEY_USER_ID, null)
        if (!existing.isNullOrBlank()) return existing

        val newId = UUID.randomUUID().toString()
        prefs.edit { putString(KEY_USER_ID, newId) }
        return newId
    }
}