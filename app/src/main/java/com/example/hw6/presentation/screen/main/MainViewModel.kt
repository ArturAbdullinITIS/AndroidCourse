package com.example.hw6.presentation.screen.main

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw6.R
import com.example.hw6.domain.model.Book
import com.example.hw6.presentation.screen.main.MainState.*
import com.example.hw6.domain.usecase.SearchBooksUseCase
import com.example.hw6.util.DataSource
import com.example.hw6.util.ResourceProvider
import com.example.hw6.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList
import kotlin.getOrDefault

private const val PAGE_SIZE = 20
private const val KEY_LAST_QUERY = "last_query"
private const val KEY_BOOKS = "cached_books"
private const val KEY_CURRENT_PAGE = "current_page"
private const val KEY_HAS_MORE_PAGES = "has_more_pages"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val resourceProvider: ResourceProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val lastQuery: StateFlow<String> = savedStateHandle.getStateFlow(KEY_LAST_QUERY, "")
    private val cachedBooks: StateFlow<List<Book>> = savedStateHandle.getStateFlow(KEY_BOOKS, emptyList())
    private val cachedPage: StateFlow<Int> = savedStateHandle.getStateFlow(KEY_CURRENT_PAGE, 0)
    private val cachedHasMore: StateFlow<Boolean> = savedStateHandle.getStateFlow(KEY_HAS_MORE_PAGES, true)

    private val _state = MutableStateFlow<MainState>(Initial)
    val state = _state.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _snackMessage = MutableSharedFlow<String>()
    val snackMessage = _snackMessage.asSharedFlow()

    private var currentQuery = ""
    private var currentPage = 0
    private var isLoadingMore = false
    private var hasMorePages = true
    private val allBooks = mutableListOf<Book>()

    init {
        restoreState()
    }

    private fun restoreState() {
        currentQuery = lastQuery.value
        _query.value = currentQuery

        allBooks.clear()
        allBooks.addAll(cachedBooks.value)

        currentPage = cachedPage.value
        hasMorePages = cachedHasMore.value

        if (currentQuery.isNotBlank() || allBooks.isNotEmpty()) {
            _state.value = Success(
                books = allBooks.toList(),
                isLoadingNextPage = false,
                hasMorePages = hasMorePages
            )
        }
    }

    private fun saveState() {
        savedStateHandle[KEY_LAST_QUERY] = currentQuery
        savedStateHandle[KEY_BOOKS] = allBooks.toList()
        savedStateHandle[KEY_CURRENT_PAGE] = currentPage
        savedStateHandle[KEY_HAS_MORE_PAGES] = hasMorePages
    }

    fun processCommand(command: MainCommand) {
        when (command) {
            is MainCommand.SearchBooks -> {
                currentQuery = command.query
                currentPage = 0
                hasMorePages = true
                allBooks.clear()
                saveState()
                loadBooks(isFirstPage = true)
            }

            MainCommand.LoadNextPage -> {
                if (!isLoadingMore && hasMorePages) {
                    loadBooks(isFirstPage = false)
                }
            }

            MainCommand.ClearInput -> {
                _query.update { "" }
            }

            is MainCommand.InputQuery -> {
                _query.update { command.query }
            }
        }
    }

    private fun loadBooks(isFirstPage: Boolean) {
        viewModelScope.launch {
            if (isFirstPage) {
                _state.update { Searching }
            } else {
                val current = _state.value
                if (current is Success) {
                    _state.update { current.copy(isLoadingNextPage = true) }
                }
            }

            if (currentQuery.isBlank()) {
                allBooks.clear()
                currentPage = 0
                hasMorePages = true
                saveState()
                _state.update { Success(books = emptyList()) }
                return@launch
            }

            isLoadingMore = true
            try {
                val startIndex = currentPage * PAGE_SIZE
                val result = searchBooksUseCase(currentQuery, startIndex, PAGE_SIZE)

                when(result) {
                    is Result.Failure -> {
                        _state.update {
                            Error(
                                result.exception.message
                                    ?: resourceProvider.getString(R.string.unknown_error)
                            )
                        }
                    }
                    is Result.Success -> {
                        val newBooks = result.data

                        if (newBooks.size < PAGE_SIZE) {
                            hasMorePages = false
                        }

                        if (newBooks.isNotEmpty()) {
                            currentPage++
                            allBooks.addAll(newBooks)
                        }

                        saveState()
                        _snackMessage.emit(
                            result.source.message
                        )
                        _state.update {
                            Success(
                                books = allBooks.toList(),
                                isLoadingNextPage = false,
                                hasMorePages = hasMorePages,
                            )
                        }
                    }
                }
            } finally {
                isLoadingMore = false
            }
        }
    }
}


sealed interface MainCommand {
    data class InputQuery(val query: String) : MainCommand
    object ClearInput : MainCommand
    data class SearchBooks(val query: String) : MainCommand
    object LoadNextPage : MainCommand
}

@Immutable
sealed class MainState {
    object Initial : MainState()
    object Searching : MainState()
    data class Success(
        val books: List<Book>,
        val isLoadingNextPage: Boolean = false,
        val hasMorePages: Boolean = true,
    ) : MainState()
    data class Error(val message: String) : MainState()
}
