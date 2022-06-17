package com.dicoding.android.intermediate.submission.storyapp.views.main

import androidx.lifecycle.ViewModel
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.UserRepository
import com.dicoding.android.intermediate.submission.storyapp.models.sessions.UserModel
import kotlinx.coroutines.flow.Flow

class MainViewModel (
    private val repository: UserRepository
) : ViewModel() {

    fun getUser(): Flow<UserModel> = repository.getUser()
}