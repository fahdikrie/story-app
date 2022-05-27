package com.dicoding.android.intermediate.submission.storyapp.views.register

import androidx.lifecycle.ViewModel
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    suspend fun postRegister(name: String, email: String, password: String) =
        repository.postRegister(name, email, password)
}