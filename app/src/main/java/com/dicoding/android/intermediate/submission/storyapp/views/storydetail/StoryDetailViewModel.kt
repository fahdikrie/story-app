package com.dicoding.android.intermediate.submission.storyapp.views.storydetail

import androidx.lifecycle.ViewModel
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.UserRepository

class StoryDetailViewModel (
    private val userRepository: UserRepository,
) : ViewModel() {

    suspend fun logout() = userRepository.logout()
}