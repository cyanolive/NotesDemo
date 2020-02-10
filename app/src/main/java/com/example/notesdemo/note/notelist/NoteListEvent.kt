package com.example.notesdemo.note.notelist

sealed class NoteListEvent {
    data class OnNoteItemClick(val position: Int) : NoteListEvent()
    object OnNewNoteClick: NoteListEvent()
    object OnStart: NoteListEvent()
}
