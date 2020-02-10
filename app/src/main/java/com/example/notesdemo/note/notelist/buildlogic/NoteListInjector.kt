package com.example.notesdemo.note.notelist.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.notesdemo.model.RoomNoteDatabase
import com.example.notesdemo.model.implementation.NoteRepoImpl
import com.example.notesdemo.model.repository.INoteRepository

class NoteListInjector(application: Application) :
    AndroidViewModel(application) {
    fun provideNoteListViewModelFactory(): NoteListViewModelFactory =
        NoteListViewModelFactory(
            getNoteRepository()
        )

    private fun getNoteRepository(): INoteRepository {
        return NoteRepoImpl(
            local = RoomNoteDatabase.getInstance(getApplication()).roomNoteDao()
        )
    }
}