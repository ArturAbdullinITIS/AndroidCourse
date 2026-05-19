package com.example.hw6.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Book(
    val id: String,
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val thumbnail: String?,
    val smallThumbnail: String?,
    val pageCount: Int,
    val averageRating: Double?
) : Parcelable