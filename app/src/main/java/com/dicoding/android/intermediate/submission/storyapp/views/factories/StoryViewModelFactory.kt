package com.dicoding.android.intermediate.submission.storyapp.views.factories

import android.content.Context
import androidx.lifecycle.ViewModelProvider

class StoryViewModelFactory(private val repo: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoryListViewModel::class.java) -> {
                StoryListViewModel(repo) as T
            }
            modelClass.isAssignableFrom(StoryUploadViewModel::class.java) -> {
                StoryUploadViewModel(repo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: StoryViewModelFactory? = null
        fun getInstance(context: Context): StoryViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: StoryViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}
