package com.dicoding.android.intermediate.submission.storyapp.models.repositories

import com.dicoding.android.intermediate.submission.storyapp.api.APIServices
import com.dicoding.android.intermediate.submission.storyapp.models.responses.StoryAddResponse
import com.dicoding.android.intermediate.submission.storyapp.models.responses.StoryListResponse
import com.dicoding.android.intermediate.submission.storyapp.utils.addBearerPrefix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val services: APIServices
    ) {

    suspend fun getStoryList(
        token: String,
        page: Int? = null,
        size: Int? = null
    ): Flow<Result<Any>> = flow {
        try {
            val bearer = addBearerPrefix(token)
            val response = services.getStoryList(bearer, page, size)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val failure = Result.failure<StoryListResponse>(e)
            emit(failure)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun postStoryItem(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Flow<Result<StoryAddResponse>> = flow {
        try {
            val bearer = addBearerPrefix(token)
            val response = services.uploadImage(bearer, file, description)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val failure = Result.failure<StoryAddResponse>(e)
            emit(failure)
        }
    }
}