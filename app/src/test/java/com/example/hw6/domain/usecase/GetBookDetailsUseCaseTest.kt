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

class GetBookDetailsUseCaseTest {
    private val repository = mockk<BookRepository>()
    private val useCase = GetBookDetailsUseCase(repository)

    @Test
    fun `invoke returns requested book from repository`() = runTest {
        val book = Book(
            id = "book-id",
            title = "Kotlin",
            authors = listOf("Author"),
            description = null,
            thumbnail = null,
            smallThumbnail = null,
            pageCount = 200,
            averageRating = 5.0,
        )
        coEvery { repository.getBookDetails("book-id") } returns
            Result.Success(book, DataSource.DATABASE)

        val result = useCase("book-id")

        assertEquals(Result.Success(book, DataSource.DATABASE), result)
        coVerify(exactly = 1) { repository.getBookDetails("book-id") }
    }
}
