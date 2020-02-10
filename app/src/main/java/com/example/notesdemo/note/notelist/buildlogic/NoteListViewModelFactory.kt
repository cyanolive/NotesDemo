package com.example.notesdemo.note.notelist.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesdemo.model.repository.INoteRepository
import com.example.notesdemo.note.notelist.NoteListViewModel
import kotlinx.coroutines.Dispatchers

class NoteListViewModelFactory(private val noteRepo: INoteRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteListViewModel(noteRepo, Dispatchers.Main) as T
    }
}