package com.example.hw6.domain.usecase

import com.example.hw6.domain.model.Book
import com.example.hw6.domain.repository.BookRepository
import com.example.hw6.util.Result
import javax.inject.Inject

class GetBookDetailsUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId: String): Result<Book> {
        return repository.getBookDetails(bookId)
    }
}