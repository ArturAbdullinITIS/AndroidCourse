package com.example.hw6.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.util.CoilUtils.result
import com.example.hw6.domain.model.Book
import com.example.hw6.domain.usecase.GetBookDetailsUseCase
import com.example.hw6.util.Result
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val BOOK_ID_KEY = "bookId"

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    private val getBookDetailsUseCase: GetBookDetailsUseCase,
    @Assisted(BOOK_ID_KEY) private val bookId: String
): ViewModel(){
    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isLoading = true
                )
            }
            val result = getBookDetailsUseCase(bookId)
            when(result) {
                is Result.Failure -> {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = result.exception.message
                        )
                    }
                }
                is Result.Success<*> -> {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            bookItem = result.data as Book
                        )
                    }
                }
            }
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(BOOK_ID_KEY) bookId: String
        ): DetailsViewModel
    }
}


data class DetailsState(
    val isLoading: Boolean = false,
    val bookItem: Book? = null,
    val errorMessage: String? = null
)