package com.dicoding.android.intermediate.submission.storyapp.views.storymap

import androidx.lifecycle.ViewModel
import com.dicoding.android.intermediate.submission.storyapp.data.repositories.StoryRepository
import com.dicoding.android.intermediate.submission.storyapp.data.repositories.UserRepository
import com.dicoding.android.intermediate.submission.storyapp.data.responses.StoryListResponse
import kotlinx.coroutines.flow.Flow

class StoryMapViewModel (
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    suspend fun getStoryListWithLocation(token: String): Flow<Result<StoryListResponse>> =
        storyRepository.getStoryWithLocationList(token)

    suspend fun logout() = userRepository.logout()
}