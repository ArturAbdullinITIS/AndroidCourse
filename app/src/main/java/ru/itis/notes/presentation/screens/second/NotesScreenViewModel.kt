package ru.itis.notes.presentation.screens.second

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import ru.itis.notes.data.NotesRepository
import ru.itis.notes.data.NotesRepositoryImpl
import ru.itis.notes.domain.GetAllNotesUseCase
import ru.itis.notes.domain.Note

class NotesScreenViewModel {


    private val notesRepository = NotesRepositoryImpl

    private val getAllNotesUseCase = GetAllNotesUseCase(notesRepository)
    private val _state = MutableStateFlow(NotesScreenState())

    val state = _state.asStateFlow()
    val scope = CoroutineScope(Dispatchers.Main)

    init {
        getAllNotesUseCase().onEach { notes ->
            _state.update {
                it.copy(notes = notes)
            }
        }.launchIn(scope)

    }
}





data class NotesScreenState(
    val notes: List<Note> = listOf()
)
