package com.example.hw6.data.mapper

import com.example.hw6.data.remote.dto.BookResponse
import com.example.hw6.domain.model.Book
import kotlin.collections.joinToString
import kotlin.text.replace
import kotlin.text.split

fun BookResponse.toDomainModel(): Book {
    return Book(
        id = this.id,
        title = this.volumeInfo.title,
        authors = this.volumeInfo.authors,
        description = this.volumeInfo.description,
        thumbnail = volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://") ?: "",
        smallThumbnail = this.volumeInfo.imageLinks?.smallThumbnail ?: "",
        pageCount = this.volumeInfo.pageCount,
        averageRating = this.volumeInfo.averageRating,
    )
}