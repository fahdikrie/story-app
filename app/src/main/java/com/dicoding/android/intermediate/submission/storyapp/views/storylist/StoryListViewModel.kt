package com.dicoding.android.intermediate.submission.storyapp.views.storylist

import androidx.lifecycle.ViewModel
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.StoryRepository
import com.dicoding.android.intermediate.submission.storyapp.models.responses.StoryListResponse
import kotlinx.coroutines.flow.Flow

class StoryListViewModel (
    private val repository: StoryRepository
) : ViewModel() {

    suspend fun getStoryList(token: String): Flow<Result<StoryListResponse>> =
        repository.getStoryList(token)
}