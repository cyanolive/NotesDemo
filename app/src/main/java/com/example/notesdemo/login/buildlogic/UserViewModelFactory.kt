package com.example.notesdemo.login.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesdemo.login.UserViewModel
import com.example.notesdemo.model.repository.IUserRepository
import kotlinx.coroutines.Dispatchers

class UserViewModelFactory(private val userRepo: IUserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(userRepo, Dispatchers.Main) as T
    }
}