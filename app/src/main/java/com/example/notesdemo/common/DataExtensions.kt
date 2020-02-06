package com.example.notesdemo.common

import android.text.Editable
import com.example.notesdemo.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal suspend fun <T> awaitTaskResult(task: Task<T>): T = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result!!)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

//Wraps Firebase/GMS calls
internal suspend fun <T> awaitTaskCompletable(task: Task<T>): Unit =
    suspendCoroutine { continuation ->
        task.addOnCompleteListener { innerTask ->
            if (innerTask.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(innerTask.exception!!)
            }
        }
    }

internal val FirebaseUser.toUser: User
    get() = User(
        uid = this.uid,
        name = this.displayName ?: ""
    )

internal fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
