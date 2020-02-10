package com.example.notesdemo.model.repository

import com.example.notesdemo.common.Result
import com.example.notesdemo.model.Note

interface INoteRepository {
    suspend fun getNotes(): Result<Exception, List<Note>>
    suspend fun getNoteById(creationDate:String): Result<Exception, Note>
    suspend fun updateNote(note: Note): Result<Exception, Unit>
    suspend fun deleteNote(note: Note): Result<Exception, Unit>
}