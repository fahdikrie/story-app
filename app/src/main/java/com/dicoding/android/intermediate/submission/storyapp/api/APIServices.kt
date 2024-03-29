package com.dicoding.android.intermediate.submission.storyapp.api

import com.dicoding.android.intermediate.submission.storyapp.data.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface APIServices {
    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("stories")
    suspend fun getStoryList(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
    ): StoryListResponse

    @GET("stories")
    suspend fun getStoryWithLocationList(
        @Header("Authorization") token: String,
        @Query("location") location: Int,
    ): StoryListResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
    ): StoryUploadResponse
}