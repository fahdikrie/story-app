package com.dicoding.android.intermediate.submission.storyapp.views.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.android.intermediate.submission.storyapp.models.injections.StoryInjection
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.StoryRepository
import com.dicoding.android.intermediate.submission.storyapp.views.storylist.StoryListViewModel
import com.dicoding.android.intermediate.submission.storyapp.views.storyupload.StoryUploadViewModel

//class StoryViewModelFactory(private val repo: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return when {
//            modelClass.isAssignableFrom(StoryListViewModel::class.java) -> {
//                StoryListViewModel(repo) as T
//            }
//            modelClass.isAssignableFrom(StoryUploadViewModel::class.java) -> {
//                StoryUploadViewModel(repo) as T
//            }
//            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//        }
//    }
//
//    companion object {
//        @Volatile
//        private var instance: StoryViewModelFactory? = null
//        fun getInstance(context: Context): StoryViewModelFactory {
//            return instance ?: synchronized(this) {
//                instance ?: StoryViewModelFactory(StoryInjection.provideRepository(context))
//            }.also { instance = it }
//        }
//    }
//}
