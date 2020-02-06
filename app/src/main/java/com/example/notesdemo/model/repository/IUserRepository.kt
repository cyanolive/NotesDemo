package com.example.notesdemo.model.repository

import com.example.notesdemo.common.Result
import com.example.notesdemo.model.User

interface IUserRepository {
    suspend fun getCurrentUser(): Result<Exception, User?>

    suspend fun signOutCurrentUser(): Result<Exception, Unit>

    suspend fun signInGoogleUser(idToken: String): Result<Exception, Unit>
}