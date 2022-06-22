package com.dicoding.android.intermediate.submission.storyapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.android.intermediate.submission.storyapp.api.APIServices
import com.dicoding.android.intermediate.submission.storyapp.data.databases.StoryDatabase
import com.dicoding.android.intermediate.submission.storyapp.data.entities.StoryEntity
import com.dicoding.android.intermediate.submission.storyapp.data.remotemediator.StoryRemoteMediator
import com.dicoding.android.intermediate.submission.storyapp.data.responses.StoryUploadResponse
import com.dicoding.android.intermediate.submission.storyapp.data.responses.StoryListResponse
import com.dicoding.android.intermediate.submission.storyapp.utils.addBearerPrefix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

@OptIn(ExperimentalPagingApi::class)
class StoryRepository constructor(
    private val services: APIServices,
    private val databases: StoryDatabase
) {

    fun getStoryList(token: String): LiveData<PagingData<StoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            remoteMediator = StoryRemoteMediator(
                databases,
                services,
                addBearerPrefix(token)
            ),
            pagingSourceFactory = {
                databases.storyDAO().getStoryList()
            }
        ).liveData
    }

    suspend fun getStoryWithLocationList(
        token: String,
        location: Int = 1,
    ): Flow<Result<StoryListResponse>> = flow {
        try {
            val bearer = addBearerPrefix(token)
            val response = services.getStoryWithLocationList(bearer, location)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val failure = Result.failure<StoryListResponse>(e)
            emit(failure)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun postStoryItem(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null,
    ): Flow<Result<StoryUploadResponse>> = flow {
        try {
            val bearer = addBearerPrefix(token)
            val response = services.uploadStory(bearer, photo, description, lat, lon)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val failure = Result.failure<StoryUploadResponse>(e)
            emit(failure)
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            services: APIServices,
            databases: StoryDatabase,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(services, databases)
            }.also { instance = it }
    }
}