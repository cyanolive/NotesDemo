package com.example.notesdemo.note.notedetail

sealed class NoteDetailEvent {
    data class OnConfirm(val contents: String) : NoteDetailEvent()
    object OnDelete : NoteDetailEvent()
    data class OnStart(val creationDate: String) : NoteDetailEvent()
}