package com.dicoding.android.intermediate.submission.storyapp.views.register

import androidx.lifecycle.ViewModel
import com.dicoding.android.intermediate.submission.storyapp.data.repositories.UserRepository

class RegisterViewModel constructor(
    private val repository: UserRepository
) : ViewModel() {

    suspend fun postRegister(name: String, email: String, password: String) =
        repository.postRegister(name, email, password)
}