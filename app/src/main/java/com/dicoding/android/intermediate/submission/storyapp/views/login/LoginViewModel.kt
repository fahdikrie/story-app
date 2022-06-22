package com.dicoding.android.intermediate.submission.storyapp.views.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.android.intermediate.submission.storyapp.data.repositories.UserRepository
import com.dicoding.android.intermediate.submission.storyapp.data.sessions.UserModel
import kotlinx.coroutines.launch

class LoginViewModel (
    private val repository: UserRepository
) : ViewModel() {

    suspend fun postLogin(email: String, password: String) =
        repository.postLogin(email, password)

    suspend fun saveUser(user: UserModel) {
        viewModelScope.launch {
            repository.saveUser(user)
        }
    }
}