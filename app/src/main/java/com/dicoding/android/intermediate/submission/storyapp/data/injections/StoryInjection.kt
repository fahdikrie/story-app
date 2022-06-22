package com.dicoding.android.intermediate.submission.storyapp.data.injections

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.android.intermediate.submission.storyapp.api.APIConfig
import com.dicoding.android.intermediate.submission.storyapp.data.databases.StoryDatabase
import com.dicoding.android.intermediate.submission.storyapp.data.repositories.StoryRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val services = APIConfig.getAPIServices()
        val databases = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(services, databases)
    }
}