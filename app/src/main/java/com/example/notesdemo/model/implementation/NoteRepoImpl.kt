package com.example.notesdemo.model.implementation

import com.example.notesdemo.common.Result
import com.example.notesdemo.common.toNote
import com.example.notesdemo.common.toNoteListFromRoomNote
import com.example.notesdemo.common.toRoomNote
import com.example.notesdemo.model.Note
import com.example.notesdemo.model.NoteDao
import com.example.notesdemo.model.repository.INoteRepository

class NoteRepoImpl(
    val local: NoteDao
) : INoteRepository {
    override suspend fun getNotes(): Result<Exception, List<Note>> {
        return getLocalNotes()
    }

    override suspend fun getNoteById(creationDate: String): Result<Exception, Note> {
        return getLocalNote(creationDate)
    }

    override suspend fun updateNote(note: Note): Result<Exception, Unit> {
        return updateLocalNote(note)
    }

    override suspend fun deleteNote(note: Note): Result<Exception, Unit> {
        return deleteLocalNote(note)
    }

    private suspend fun getLocalNotes(): Result<Exception, List<Note>> = Result.build {
        local.getNotes().toNoteListFromRoomNote()
    }

    private suspend fun getLocalNote(creationDate: String): Result<Exception, Note> = Result.build {
        local.getNoteById(creationDate).toNote
    }

    private suspend fun updateLocalNote(note: Note): Result<Exception, Unit> = Result.build {
        local.upsertNote(note.toRoomNote)
        Unit
    }

    private suspend fun deleteLocalNote(note: Note): Result<Exception, Unit> = Result.build {
        local.deleteNote(note.toRoomNote)
    }
}