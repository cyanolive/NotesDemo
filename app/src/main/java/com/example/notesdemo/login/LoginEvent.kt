package com.example.notesdemo.login

sealed class LoginEvent<out T> {
    object OnAuthButtonClick : LoginEvent<Nothing>()
    object OnStart : LoginEvent<Nothing>()
    data class OnGoogleSignInResult<out LoginResult>(val result: LoginResult) : LoginEvent<LoginResult>()
}