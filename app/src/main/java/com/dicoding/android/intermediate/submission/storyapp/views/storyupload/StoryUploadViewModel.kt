package com.dicoding.android.intermediate.submission.storyapp.views.storyupload

import androidx.lifecycle.ViewModel
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.StoryRepository
import okhttp3.MultipartBody

class StoryUploadViewModel constructor(
    private val repository: StoryRepository
) : ViewModel() {

    suspend fun postStoryItem(token: String, photo: MultipartBody.Part, description: String) =
        repository.postStoryItem(token, photo, description)
}