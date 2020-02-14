package com.example.notesdemo.note.notedetail.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesdemo.model.repository.INoteRepository
import com.example.notesdemo.note.notedetail.NoteDetailViewModel
import kotlinx.coroutines.Dispatchers

class NoteDetailViewModelFactory(private val noteRepo: INoteRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteDetailViewModel(noteRepo, Dispatchers.Main) as T
    }
}