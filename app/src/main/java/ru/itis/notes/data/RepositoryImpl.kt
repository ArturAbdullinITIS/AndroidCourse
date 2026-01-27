package ru.itis.notes.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.itis.notes.domain.Note

object NotesRepositoryImpl: NotesRepository {


    private val notes = MutableStateFlow<List<Note>>(listOf())



    override fun addNote(title: String, content: String) {
        notes.update { oldList ->
            val note = Note(oldList.size, title, content)
            oldList + note
        }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notes.asStateFlow()
    }

}