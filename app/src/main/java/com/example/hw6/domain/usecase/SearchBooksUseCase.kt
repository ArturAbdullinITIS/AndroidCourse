package com.example.hw6.domain.usecase


import com.example.hw6.domain.model.Book
import com.example.hw6.domain.repository.BookRepository
import com.example.hw6.util.Result
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(
        query: String,
        startIndex: Int = 0,
        maxResults: Int = 20
    ): Result<List<Book>> {
        return repository.searchBooks(query, startIndex, maxResults)
    }
}