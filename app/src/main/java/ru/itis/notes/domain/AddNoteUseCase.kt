package ru.itis.notes.domain

import ru.itis.notes.data.NotesRepository

class AddNoteUseCase(private val repo: NotesRepository) {

    operator fun invoke(
        title: String,
        content: String
    ) {
        repo.addNote(
            title = title,
            content = content
        )
    }
}