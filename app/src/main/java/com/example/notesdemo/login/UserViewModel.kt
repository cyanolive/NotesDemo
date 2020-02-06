package com.example.notesdemo.login

import androidx.lifecycle.MutableLiveData
import com.example.notesdemo.common.*
import com.example.notesdemo.model.LoginResult
import com.example.notesdemo.model.User
import com.example.notesdemo.model.repository.IUserRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserViewModel(
    private val repo: IUserRepository,
    uiContext: CoroutineContext
) : BaseViewModel<LoginEvent<LoginResult>>(uiContext) {
    private val userState = MutableLiveData<User>()

    //Control Logic
    internal val authAttempt = MutableLiveData<Unit>()
    internal val startAnimation = MutableLiveData<Unit>()

    //UI Binding
    internal val signInStatusText = MutableLiveData<String>()
    internal val authButtonText = MutableLiveData<String>()
    internal val satelliteDrawable = MutableLiveData<String>()

    private fun showErrorState() {
        signInStatusText.value = LOGIN_ERROR
        authButtonText.value = SIGN_IN
        satelliteDrawable.value = ANTENNA_EMPTY
    }

    private fun showLoadingState() {
        signInStatusText.value = LOADING
        satelliteDrawable.value = ANTENNA_LOOP
        startAnimation.value = Unit
    }

    private fun showSignedInState() {
        signInStatusText.value = SIGNED_IN
        authButtonText.value = SIGN_OUT
        satelliteDrawable.value = ANTENNA_FULL
    }

    private fun showSignedOutState() {
        signInStatusText.value = SIGNED_OUT
        authButtonText.value = SIGN_IN
        satelliteDrawable.value = ANTENNA_EMPTY
    }

    override fun handleEvent(event: LoginEvent<LoginResult>) {
        showLoadingState()
        when (event) {
            is LoginEvent.OnStart -> getUser()
            is LoginEvent.OnAuthButtonClick -> onAuthButtonClick()
            is LoginEvent.OnGoogleSignInResult -> onSignInResult(event.result)
        }
    }

    private fun onSignInResult(result: LoginResult) = launch {
        if (result.requestCode == RC_SIGN_IN && result.userToken != null) {
            val createGoogleUserResult = repo.signInGoogleUser(result.userToken)
            if (createGoogleUserResult is Result.Value) {
                getUser()
            } else {
                showErrorState()
            }
        } else {
            showErrorState()
        }
    }

    private fun onAuthButtonClick() {
        if (userState.value == null) {
            authAttempt.value = Unit
        } else {
            signOutUser()
        }
    }

    private fun signOutUser() = launch {
        when (repo.signOutCurrentUser()) {
            is Result.Value -> {
                userState.value = null
                showSignedOutState()
            }
            is Result.Error -> showErrorState()
        }
    }

    private fun getUser() = launch {
        when (val result = repo.getCurrentUser()) {
            is Result.Value -> {
                userState.value = result.value
                if (result.value == null) showSignedOutState()
                else showSignedInState()
            }
        }
    }
}
