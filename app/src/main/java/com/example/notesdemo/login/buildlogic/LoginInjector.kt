package com.example.notesdemo.login.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.notesdemo.model.implementation.FirebaseUserRepoImpl
import com.example.notesdemo.model.repository.IUserRepository

class LoginInjector(application: Application) : AndroidViewModel(application) {

    fun provideUserViewModelFactory(): UserViewModelFactory =
        UserViewModelFactory(
            getUserRepository()
        )

    private fun getUserRepository(): IUserRepository {
        return FirebaseUserRepoImpl()
    }
}