package ru.itis.notes.data

import kotlinx.coroutines.flow.Flow
import ru.itis.notes.domain.Note

interface NotesRepository {
    fun addNote(title: String, content: String)
    fun getAllNotes(): Flow<List<Note>>
}