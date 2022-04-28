package com.dicoding.android.intermediate.submission.storyapp.api

import com.dicoding.android.intermediate.submission.storyapp.models.responses.LoginResponse
import com.dicoding.android.intermediate.submission.storyapp.models.responses.RegisterResponse
import com.dicoding.android.intermediate.submission.storyapp.models.responses.StoryListResponse
import retrofit2.Call
import retrofit2.http.*

interface APIServices {
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getListStories(
        @Header("Authorization") token: String
    ):Call<StoryListResponse>
}