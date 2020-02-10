package com.example.notesdemo.note.notelist

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.example.notesdemo.model.Note

class NoteDiffUtilCallback : ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.creationDate == newItem.creationDate
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.creationDate == newItem.creationDate
    }
}