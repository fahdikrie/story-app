package com.dicoding.android.intermediate.submission.storyapp.views.main

import androidx.lifecycle.ViewModel
import com.dicoding.android.intermediate.submission.storyapp.data.repositories.UserRepository
import com.dicoding.android.intermediate.submission.storyapp.data.sessions.UserModel
import kotlinx.coroutines.flow.Flow

class MainViewModel (
    private val repository: UserRepository
) : ViewModel() {

    fun getUser(): Flow<UserModel> = repository.getUser()
}