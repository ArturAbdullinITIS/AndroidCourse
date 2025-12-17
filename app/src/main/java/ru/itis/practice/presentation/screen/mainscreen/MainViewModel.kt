package ru.itis.practice.presentation.screen.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import ru.itis.practice.domain.entity.Movie
import ru.itis.practice.domain.usecase.GetAllMoviesUseCase
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllMoviesUseCase: GetAllMoviesUseCase
): ViewModel() {
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()


    init {
        getAllMoviesUseCase().onEach { movies ->
            _state.update { state->
                state.copy(
                    movies = movies
                )
            }
        }.launchIn(viewModelScope)
    }

}


sealed interface MainScreenCommand {

}



data class MainScreenState(
    val movies: List<Movie> = listOf()
)