package ru.itis.practice.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
): ResourceProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}