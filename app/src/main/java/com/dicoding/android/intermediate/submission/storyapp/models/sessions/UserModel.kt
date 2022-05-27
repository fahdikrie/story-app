package com.dicoding.android.intermediate.submission.storyapp.models.sessions

data class UserModel(
    val name: String,
    val email: String,
    val password: String,
    val token: String,
    val isLogin: Boolean,
)