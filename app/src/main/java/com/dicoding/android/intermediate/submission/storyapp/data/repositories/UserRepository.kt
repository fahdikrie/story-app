package com.dicoding.android.intermediate.submission.storyapp.data.repositories

import android.util.Log
import com.dicoding.android.intermediate.submission.storyapp.api.APIServices
import com.dicoding.android.intermediate.submission.storyapp.data.responses.LoginResponse
import com.dicoding.android.intermediate.submission.storyapp.data.responses.RegisterResponse
import com.dicoding.android.intermediate.submission.storyapp.data.sessions.UserModel
import com.dicoding.android.intermediate.submission.storyapp.data.sessions.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository constructor(
    private val preferences: UserPreferences,
    private val services: APIServices
) {
    suspend fun postLogin(email: String, password: String): Flow<Result<Any>> = flow {
        try {
            val response = services.postLogin(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val failure = Result.failure<LoginResponse>(e)
            emit(failure)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): Flow<Result<Any>> = flow {
        try {
            val response = services.postRegister(name, email, password)
            Log.d(Log.INFO.toString(), response.message)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(Log.ERROR.toString(), "error")
            val failure = Result.failure<RegisterResponse>(e)
            emit(failure)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveUser(user: UserModel) = preferences.saveUser(user)

    suspend fun logout() = preferences.logout()

    fun getUser() = preferences.getUser()

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            preferences: UserPreferences,
            services: APIServices
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(preferences, services)
            }.also { instance = it }
    }
}