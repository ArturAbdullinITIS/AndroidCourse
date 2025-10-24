package ru.itis.notes.domain

import kotlinx.coroutines.flow.Flow
import ru.itis.notes.data.NotesRepository

class GetAllNotesUseCase(private val repo: NotesRepository) {

    fun invoke(): Flow<List<Note>> {
        return repo.getAllNotes()
    }
}