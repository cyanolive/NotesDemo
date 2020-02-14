package com.example.notesdemo.note.notedetail.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.notesdemo.model.RoomNoteDatabase
import com.example.notesdemo.model.implementation.NoteRepoImpl
import com.example.notesdemo.model.repository.INoteRepository

class NoteDetailInjector(application: Application) : AndroidViewModel(application) {
    fun provideNoteDetailViewModelFactory(): NoteDetailViewModelFactory =
        NoteDetailViewModelFactory(
            getNoteRepository()
        )

    private fun getNoteRepository(): INoteRepository {
        return NoteRepoImpl(
            local = RoomNoteDatabase.getInstance(getApplication()).roomNoteDao()
        )
    }
}