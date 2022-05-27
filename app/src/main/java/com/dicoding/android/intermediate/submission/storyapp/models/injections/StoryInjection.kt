package com.dicoding.android.intermediate.submission.storyapp.models.injections

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.android.intermediate.submission.storyapp.api.APIConfig
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.StoryRepository
import com.dicoding.android.intermediate.submission.storyapp.models.sessions.UserPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val services = APIConfig.getAPIServices()
        return StoryRepository.getInstance(services)
    }
}