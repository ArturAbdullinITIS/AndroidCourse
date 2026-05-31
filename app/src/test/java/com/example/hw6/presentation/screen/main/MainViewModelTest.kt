package com.example.hw6.presentation.screen.main

import androidx.lifecycle.SavedStateHandle
import com.example.hw6.MainDispatcherRule
import com.example.hw6.domain.model.Book
import com.example.hw6.domain.usecase.SearchBooksUseCase
import com.example.hw6.util.DataSource
import com.example.hw6.util.ResourceProvider
import com.example.hw6.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val searchBooksUseCase = mockk<SearchBooksUseCase>()
    private val resourceProvider = mockk<ResourceProvider>()
    private val viewModel = MainViewModel(
        searchBooksUseCase = searchBooksUseCase,
        resourceProvider = resourceProvider,
        savedStateHandle = SavedStateHandle(),
    )

    @Test
    fun `input query updates query state`() {
        viewModel.processCommand(MainCommand.InputQuery("compose"))

        assertEquals("compose", viewModel.query.value)
    }

    @Test
    fun `search books puts returned books into state`() {
        val books = listOf(book("compose"))
        coEvery { searchBooksUseCase("compose", 0, 20) } returns
            Result.Success(books, DataSource.API)

        viewModel.processCommand(MainCommand.SearchBooks("compose"))

        assertEquals(
            MainState.Success(books = books, hasMorePages = false),
            viewModel.state.value,
        )
        coVerify(exactly = 1) { searchBooksUseCase("compose", 0, 20) }
    }

    private fun book(id: String) = Book(
        id = id,
        title = "Jetpack Compose",
        authors = listOf("Author"),
        description = null,
        thumbnail = null,
        smallThumbnail = null,
        pageCount = 300,
        averageRating = 4.8,
    )
}
