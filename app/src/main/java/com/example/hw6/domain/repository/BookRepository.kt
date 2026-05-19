package com.example.hw6.domain.repository

import com.example.hw6.domain.model.Book
import com.example.hw6.util.Result

interface BookRepository {
    suspend fun searchBooks(
        query: String,
        startIndex: Int = 0,
        maxResults: Int = 20
    ): Result<List<Book>>
    suspend fun getBookDetails(bookId: String): Result<Book>
}