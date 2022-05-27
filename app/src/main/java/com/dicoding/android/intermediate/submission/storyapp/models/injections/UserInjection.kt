package com.dicoding.android.intermediate.submission.storyapp.models.injections

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.android.intermediate.submission.storyapp.api.APIConfig
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.StoryRepository
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.UserRepository
import com.dicoding.android.intermediate.submission.storyapp.models.sessions.UserPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object UserInjection {
    fun provideRepository(context: Context): UserRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val services = APIConfig.getAPIServices()
        return UserRepository.getInstance(preferences, services)
    }
}