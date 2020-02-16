package com.example.notesdemo.model.implementation

import com.example.notesdemo.common.*
import com.example.notesdemo.model.FirebaseNote
import com.example.notesdemo.model.Note
import com.example.notesdemo.model.NoteDao
import com.example.notesdemo.model.User
import com.example.notesdemo.model.repository.INoteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

private const val COLLECTION_NAME = "notes"

class NoteRepoImpl(
    val local: NoteDao,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val remote: FirebaseFirestore = FirebaseFirestore.getInstance()
) : INoteRepository {
    override suspend fun getNotes(): Result<Exception, List<Note>> {
        val user = getActiveUser()
        return if (user != null) getRemoteNotes(user)
        else getLocalNotes()
    }

    override suspend fun getNoteById(creationDate: String): Result<Exception, Note> {
        val user = getActiveUser()
        return if (user != null) getRemoteNote(creationDate, user)
        else getLocalNote(creationDate)
    }

    override suspend fun updateNote(note: Note): Result<Exception, Unit> {
        val user = getActiveUser()
        return if (user != null) updateRemoteNote(note.copy(creator = user))
        else updateLocalNote(note)
    }

    override suspend fun deleteNote(note: Note): Result<Exception, Unit> {
        val user = getActiveUser()
        return if (user != null) deleteRemoteNote(note.copy(creator = user))
        else deleteLocalNote(note)
    }

    private suspend fun getLocalNotes(): Result<Exception, List<Note>> = Result.build {
        local.getNotes().toNoteListFromRoomNote()
    }

    private suspend fun getLocalNote(creationDate: String): Result<Exception, Note> = Result.build {
        local.getNoteById(creationDate).toNote
    }

    private suspend fun updateLocalNote(note: Note): Result<Exception, Unit> = Result.build {
        local.upsertNote(note.toRoomNote)
        Unit
    }

    private suspend fun deleteLocalNote(note: Note): Result<Exception, Unit> = Result.build {
        local.deleteNote(note.toRoomNote)
    }

    private suspend fun getRemoteNotes(user: User): Result<Exception, List<Note>> {
        return try {
            val task = awaitTaskResult(
                remote.collection(COLLECTION_NAME).whereEqualTo("creator", user.uid).get()
            )
            resultToNoteList(task)
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

    private suspend fun getRemoteNote(creationDate: String, user: User): Result<Exception, Note> {
        return try {
            val task = awaitTaskResult(
                remote.collection(COLLECTION_NAME).document(creationDate + user).get()
            )
            return Result.build {
                task.toObject(FirebaseNote::class.java)?.toNote ?: throw Exception()
            }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

    private suspend fun updateRemoteNote(note: Note): Result<Exception, Unit> =
        Result.build {
            awaitTaskCompletable(
                remote.collection(COLLECTION_NAME)
                    .document(note.creationDate + note.creator!!.uid)
                    .set(note.toFirebaseNote)
            )
        }

    private suspend fun deleteRemoteNote(note: Note): Result<Exception, Unit> =
        Result.build {
            awaitTaskCompletable(
                remote.collection(COLLECTION_NAME)
                    .document(note.creationDate + note.creator!!.uid)
                    .delete()
            )
        }

    /**
     * if currentUser != null, return true
     */
    private fun getActiveUser(): User? {
        return auth.currentUser?.toUser
    }

    private fun
            resultToNoteList(result: QuerySnapshot?): Result<Exception, List<Note>> {
        val noteList = mutableListOf<Note>()

        result?.forEach { documentSnapshot ->
            noteList.add(documentSnapshot.toObject(FirebaseNote::class.java).toNote)
        }

        return Result.build {
            noteList
        }
    }

}