package com.dicoding.android.intermediate.submission.storyapp.views.storylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.dicoding.android.intermediate.submission.storyapp.data.entities.StoryEntity
import com.dicoding.android.intermediate.submission.storyapp.data.repositories.StoryRepository
import com.dicoding.android.intermediate.submission.storyapp.data.repositories.UserRepository

class StoryListViewModel (
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    fun getStoryList(token: String): LiveData<PagingData<StoryEntity>> =
        storyRepository.getStoryList(token)

    suspend fun logout() = userRepository.logout()
}