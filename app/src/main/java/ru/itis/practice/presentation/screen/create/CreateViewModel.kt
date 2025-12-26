package ru.itis.practice.presentation.screen.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.practice.domain.usecase.AddMovieUseCase
import ru.itis.practice.domain.usecase.MovieField
import ru.itis.practice.domain.usecase.MovieValidationResult
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val addMovieUseCase: AddMovieUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateScreenState())
    val state = _state.asStateFlow()

    fun processCommand(command: CreateScreenCommand) {
        when (command) {
            is CreateScreenCommand.InputTitle -> {
                _state.update { it.copy(title = command.title) }
            }
            is CreateScreenCommand.InputDescription -> {
                _state.update { it.copy(description = command.description) }
            }
            is CreateScreenCommand.InputRating -> {
                _state.update { it.copy(rating = command.rating) }
            }
            is CreateScreenCommand.InputYear -> {
                _state.update { it.copy(releaseYear = command.year) }
            }
            is CreateScreenCommand.Submit -> {
                submitMovie()
            }
        }
    }

    private fun submitMovie() {
        val currentState = _state.value
        viewModelScope.launch {
            val result = addMovieUseCase(
                title = currentState.title,
                description = currentState.description,
                year = currentState.releaseYear,
                rating = currentState.rating
            )

            if (result.isValid) {
                _state.update { it.copy(success = true) }
            } else {
                _state.update { state ->
                    state.copy(
                        titleError = result.errors[MovieField.TITLE],
                        descriptionError = result.errors[MovieField.DESCRIPTION],
                        ratingError = result.errors[MovieField.RATING],
                        yearError = result.errors[MovieField.YEAR],
                        dbError = result.dbError
                    )
                }
            }
        }
    }
}

sealed interface CreateScreenCommand {
    data class InputTitle(val title: String) : CreateScreenCommand
    data class InputDescription(val description: String) : CreateScreenCommand
    data class InputRating(val rating: String) : CreateScreenCommand
    data class InputYear(val year: String) : CreateScreenCommand
    data object Submit : CreateScreenCommand
}

data class CreateScreenState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val rating: String = "",
    val ratingError: String? = null,
    val releaseYear: String = "",
    val yearError: String? = null,
    val success: Boolean = false,
    val dbError: String? = null
)
