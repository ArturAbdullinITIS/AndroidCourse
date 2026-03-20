package com.example.hw6.util

import android.content.Context
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceProvider(
    private val context: Context
) {
    fun getString(resId: Int): String {
        return context.getString(resId)
    }
}