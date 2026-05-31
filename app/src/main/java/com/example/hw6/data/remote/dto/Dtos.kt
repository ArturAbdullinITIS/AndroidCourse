package com.example.hw6.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BooksResponse(
    @SerialName("items")
    val items: List<BookResponse>?,
    @SerialName("totalItems")
    val totalItems: Int
)

@Serializable
data class BookResponse(
    @SerialName("id")
    val id: String,
    @SerialName("volumeInfo")
    val volumeInfo: VolumeInfoResponse
)

@Serializable
data class VolumeInfoResponse(
    @SerialName("title")
    val title: String,
    @SerialName("authors")
    val authors: List<String> = emptyList(),
    @SerialName("description")
    val description: String?,
    @SerialName("imageLinks")
    val imageLinks: ImageLinksResponse?,
    @SerialName("pageCount")
    val pageCount: Int,
    @SerialName("averageRating")
    val averageRating: Double?
)
@Serializable
data class ImageLinksResponse(
    @SerialName("thumbnail")
    val thumbnail: String?,
    @SerialName("smallThumbnail")
    val smallThumbnail: String?
)

