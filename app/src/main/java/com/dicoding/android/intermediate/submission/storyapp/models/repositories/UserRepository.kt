package com.dicoding.android.intermediate.submission.storyapp.models.repositories

import com.dicoding.android.intermediate.submission.storyapp.api.APIServices
import com.dicoding.android.intermediate.submission.storyapp.models.responses.LoginResponse
import com.dicoding.android.intermediate.submission.storyapp.models.responses.RegisterResponse
import com.dicoding.android.intermediate.submission.storyapp.models.sessions.UserModel
import com.dicoding.android.intermediate.submission.storyapp.models.sessions.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(
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
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val failure = Result.failure<RegisterResponse>(e)
            emit(failure)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveUser(user: UserModel) {
        preferences.saveUser(user)
    }

    fun getUser() = preferences.getUser()

    suspend fun login() {
        preferences.login()
    }

    suspend fun logout() {
        preferences.logout()
    }
}