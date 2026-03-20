package com.example.hw6.presentation.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable



@Parcelize
sealed interface Route : Parcelable

@Parcelize
data object Main: Route


@Parcelize
data class Details(
    val bookId: String
): Route