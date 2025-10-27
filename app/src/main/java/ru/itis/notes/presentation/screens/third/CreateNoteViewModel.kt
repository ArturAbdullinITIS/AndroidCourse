package ru.itis.notes.presentation.screens.third

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.itis.notes.data.NotesRepositoryImpl
import ru.itis.notes.domain.AddNoteUseCase

class CreateNoteViewModel {
    private val notesRepository = NotesRepositoryImpl
    private val addNotesUseCase = AddNoteUseCase(notesRepository)

    private val _state = MutableStateFlow(CreateNoteState())
    val state = _state.asStateFlow()

    fun processCommand(command: CreateNotesCommand) {
        when(command) {
            is CreateNotesCommand.InputContent -> {
                _state.update { state ->
                    state.copy(
                        content = command.content,
                        isEnabled = state.title.isNotBlank()
                    )
                }
            }
            is CreateNotesCommand.InputTitle -> {
                _state.update { state ->
                    state.copy(
                        title = command.title,
                        isEnabled = command.title.isNotBlank()
                    )
                }
            }
            CreateNotesCommand.Save -> {
                _state.update { state ->
                    val title = state.title
                    val content = state.content
                    addNotesUseCase(title, content)
                    state.copy(
                        title = "",
                        content = "",
                        isEnabled = false
                    )
                }
            }
        }
    }
}


sealed interface CreateNotesCommand {
    data class InputTitle(val title: String): CreateNotesCommand
    data class InputContent(val content: String): CreateNotesCommand
    data object Save: CreateNotesCommand
}




data class CreateNoteState(
    val isEnabled: Boolean = false,
    val title: String = "",
    val content: String = ""
)

