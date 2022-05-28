package com.dicoding.android.intermediate.submission.storyapp.models.repositories

import android.util.Log
import com.dicoding.android.intermediate.submission.storyapp.api.APIServices
import com.dicoding.android.intermediate.submission.storyapp.models.responses.StoryAddResponse
import com.dicoding.android.intermediate.submission.storyapp.models.responses.StoryListResponse
import com.dicoding.android.intermediate.submission.storyapp.models.sessions.UserPreferences
import com.dicoding.android.intermediate.submission.storyapp.utils.addBearerPrefix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository constructor(
    private val services: APIServices
) {

    suspend fun getStoryList(
        token: String,
        page: Int? = null,
        size: Int? = null
    ): Flow<Result<StoryListResponse>> = flow {
        try {
            val bearer = addBearerPrefix(token)
            val response = services.getStoryList(bearer, page, size)
            Log.d(Log.WARN.toString(), "TESSSS")
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(Log.WARN.toString(), "TESSSS")
            val failure = Result.failure<StoryListResponse>(e)
            emit(failure)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun postStoryItem(
        token: String,
        photo: MultipartBody.Part,
        description: String,
    ): Flow<Result<StoryAddResponse>> = flow {
        try {
            val bearer = addBearerPrefix(token)
            val response = services.uploadImage(bearer, photo, description)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val failure = Result.failure<StoryAddResponse>(e)
            emit(failure)
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            services: APIServices
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(services)
            }.also { instance = it }
    }
}