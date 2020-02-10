package com.example.notesdemo.note.notelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesdemo.common.BaseViewModel
import com.example.notesdemo.common.GET_NOTES_ERROR
import com.example.notesdemo.common.Result
import com.example.notesdemo.model.Note
import com.example.notesdemo.model.repository.INoteRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NoteListViewModel(
    private val noteRepo: INoteRepository,
    uiContext: CoroutineContext
) : BaseViewModel<NoteListEvent>(uiContext) {

    private val noteListState = MutableLiveData<List<Note>>()
    val notelist: LiveData<List<Note>> get() = noteListState

    private val editNoteState = MutableLiveData<String>()
    val editNote: LiveData<String> get() = editNoteState

    override fun handleEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.OnStart -> getNotes()
            is NoteListEvent.OnNoteItemClick -> editNote(event.position)
        }
    }

    private fun editNote(position: Int) {
        editNoteState.value = notelist.value!![position].creationDate
    }

    private fun getNotes() = launch {
        when (val notesResult = noteRepo.getNotes()) {
            is Result.Value -> noteListState.value = notesResult.value
            is Result.Error -> errorState.value = GET_NOTES_ERROR
        }
    }
}