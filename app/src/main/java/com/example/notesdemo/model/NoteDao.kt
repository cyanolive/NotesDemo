package com.example.notesdemo.model

import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    suspend fun getNotes(): List<RoomNote>

    @Query("SELECT * FROM notes WHERE creation_date = :creationDate")
    suspend fun getNoteById(creationDate: String): RoomNote

    @Delete
    suspend fun deleteNote(note: RoomNote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(note: RoomNote): Long
}