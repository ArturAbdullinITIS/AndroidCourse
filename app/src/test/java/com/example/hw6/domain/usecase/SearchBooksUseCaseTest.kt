package com.example.hw6.domain.usecase

import com.example.hw6.domain.model.Book
import com.example.hw6.domain.repository.BookRepository
import com.example.hw6.util.DataSource
import com.example.hw6.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchBooksUseCaseTest {
    private val repository = mockk<BookRepository>()
    private val useCase = SearchBooksUseCase(repository)

    @Test
    fun `invoke returns books from repository for query`() = runTest {
        val books = listOf(book("android"))
        coEvery { repository.searchBooks("android", 0, 20) } returns
            Result.Success(books, DataSource.API)

        val result = useCase("android")

        assertEquals(Result.Success(books, DataSource.API), result)
        coVerify(exactly = 1) { repository.searchBooks("android", 0, 20) }
    }

    private fun book(id: String) = Book(
        id = id,
        title = "Android",
        authors = listOf("Author"),
        description = null,
        thumbnail = null,
        smallThumbnail = null,
        pageCount = 100,
        averageRating = 4.5,
    )
}
