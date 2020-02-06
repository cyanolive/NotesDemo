package com.example.notesdemo.model.implementation

import com.example.notesdemo.common.Result
import com.example.notesdemo.common.awaitTaskCompletable
import com.example.notesdemo.model.User
import com.example.notesdemo.model.repository.IUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseUserRepoImpl(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) : IUserRepository {
    override suspend fun getCurrentUser(): Result<Exception, User?> {
        val firebaseUser = auth.currentUser
        return if (firebaseUser == null) {
            Result.build { null }
        } else {
            Result.build {
                User(firebaseUser.uid, firebaseUser.displayName ?: "")
            }
        }
    }

    override suspend fun signOutCurrentUser(): Result<Exception, Unit> {
        return Result.build {
            auth.signOut()
        }
    }

    override suspend fun signInGoogleUser(idToken: String):
            Result<Exception, Unit> = withContext(Dispatchers.IO) {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            awaitTaskCompletable(auth.signInWithCredential(credential))
            Result.build { Unit }
        } catch (exception: java.lang.Exception) {
            Result.build { throw exception }
        }
    }
}