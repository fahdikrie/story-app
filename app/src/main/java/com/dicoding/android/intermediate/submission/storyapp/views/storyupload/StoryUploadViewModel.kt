package com.dicoding.android.intermediate.submission.storyapp.views.storyupload

import androidx.lifecycle.ViewModel
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.StoryRepository
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryUploadViewModel constructor(
    private val repository: StoryRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    suspend fun postStoryItem(token: String, photo: MultipartBody.Part, description: RequestBody) =
        repository.postStoryItem(token, photo, description)

    suspend fun logout() = userRepository.logout()
}