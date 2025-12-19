package ru.itis.practice.presentation.screen.mainscreen

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.practice.domain.entity.Movie
import ru.itis.practice.domain.usecase.DeleteMovieUseCase
import ru.itis.practice.domain.usecase.GetAllMoviesUseCase
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase
): ViewModel() {
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    val sortedMovies: StateFlow<List<Movie>> = getAllMoviesUseCase()
        .combine(state) { movies, currentState ->
            when(currentState.sortOrder) {
                SortOrder.BY_TITLE -> movies.sortedBy { it.title }
                SortOrder.BY_RATING -> movies.sortedBy { it.rating }
                SortOrder.BY_YEAR -> movies.sortedBy { it.releaseYear }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun processCommand(command: MainScreenCommand) {
        when(command) {
            MainScreenCommand.CancelDelete -> {
                _state.update { state ->
                    state.copy(
                        movieToDelete = null
                    )
                }
            }
            is MainScreenCommand.ConfirmDelete -> {
                viewModelScope.launch {
                    val movieId = _state.value.movieToDelete?.id
                    deleteMovieUseCase(movieId)
                    _state.update { state->
                        state.copy(
                            movieToDelete = null
                        )
                    }
                }
            }
            is MainScreenCommand.OnCardLongClick -> {
                _state.update { state->
                    state.copy(
                        movieToDelete = command.movie
                    )
                }
            }

            is MainScreenCommand.ChangeSortOrder -> {
                _state.update { state ->
                    state.copy(
                        sortOrder = command.sortOrder
                    )
                }
            }
        }
    }

}


sealed interface MainScreenCommand {
    data class OnCardLongClick(val movie: Movie) : MainScreenCommand
    data object ConfirmDelete : MainScreenCommand
    data object CancelDelete : MainScreenCommand
    data class ChangeSortOrder(val sortOrder: SortOrder) : MainScreenCommand
}


data class MainScreenState(
    val movieToDelete: Movie? = null,
    val sortOrder: SortOrder = SortOrder.BY_TITLE,
)

enum class SortOrder {
    BY_TITLE, BY_RATING, BY_YEAR
}
