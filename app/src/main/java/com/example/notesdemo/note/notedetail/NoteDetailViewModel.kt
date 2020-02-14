package com.example.notesdemo.note.notedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesdemo.common.*
import com.example.notesdemo.model.Note
import com.example.notesdemo.model.implementation.NoteRepoImpl
import com.example.notesdemo.model.repository.INoteRepository
import com.example.notesdemo.note.notedetail.NoteDetailEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class NoteDetailViewModel(private val noteRepo: INoteRepository, uiContext: CoroutineContext) :
    BaseViewModel<NoteDetailEvent>(uiContext) {
    private val noteState = MutableLiveData<Note>()
    val note: LiveData<Note> get() = noteState

    private val confirmedState = MutableLiveData<Boolean>()
    val confirmed: LiveData<Boolean> get() = confirmedState

    private val deletedState = MutableLiveData<Boolean>()
    val deleted: LiveData<Boolean> get() = deletedState

    override fun handleEvent(event: NoteDetailEvent) {
        when (event) {
            is NoteDetailEvent.OnConfirm -> updateNote(event.contents)
            is NoteDetailEvent.OnDelete -> deleteNote()
            is NoteDetailEvent.OnStart -> getNote(event.creationDate)
        }
    }

    private fun getNote(creationDate: String) = launch {
        if (creationDate == "") newNote()
        else {
            when (val noteResult = noteRepo.getNoteById(creationDate)) {
                is Result.Value -> noteState.value = noteResult.value
                is Result.Error -> errorState.value = GET_NOTE_ERROR
            }
        }
    }

    private fun newNote() {
        noteState.value = Note(getCalendarTime(), "", 0, "rocket_loop", null)
    }

    private fun getCalendarTime(): String {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val format = SimpleDateFormat("d MMM yyyy HH:mm:ss Z", Locale.getDefault())
        format.timeZone = cal.timeZone
        return format.format(cal.time)
    }

    private fun deleteNote() = launch {
        when (noteRepo.deleteNote(note.value!!)) {
            is Result.Value -> deletedState.value = true
            is Result.Error -> errorState.value = DELETE_NOTE_ERROR
        }
    }

    private fun updateNote(contents: String) = launch {
        when (noteRepo.updateNote(noteState.value!!.copy(contents = contents))) {
            is Result.Value -> confirmedState.value = true
            is Result.Error -> errorState.value = UPDATE_NOTE_ERROR
        }
    }
}