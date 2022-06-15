package com.dicoding.android.intermediate.submission.storyapp.views.storylist

import androidx.lifecycle.ViewModel
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.StoryRepository
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.UserRepository
import com.dicoding.android.intermediate.submission.storyapp.models.responses.StoryListResponse
import kotlinx.coroutines.flow.Flow

class StoryListViewModel (
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    suspend fun getStoryList(token: String): Flow<Result<StoryListResponse>> =
        storyRepository.getStoryList(token)

    suspend fun logout() = userRepository.logout()
}