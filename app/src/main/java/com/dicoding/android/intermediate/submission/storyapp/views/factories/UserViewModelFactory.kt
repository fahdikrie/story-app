package com.dicoding.android.intermediate.submission.storyapp.views.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.android.intermediate.submission.storyapp.models.injections.UserInjection
import com.dicoding.android.intermediate.submission.storyapp.models.repositories.UserRepository
import com.dicoding.android.intermediate.submission.storyapp.views.login.LoginViewModel
import com.dicoding.android.intermediate.submission.storyapp.views.main.MainViewModel
import com.dicoding.android.intermediate.submission.storyapp.views.register.RegisterViewModel

class UserViewModelFactory(private val repo: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                RegisterViewModel(repo) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repo) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: UserViewModelFactory? = null
        fun getInstance(context: Context): UserViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: UserViewModelFactory(UserInjection.provideRepository(context))
            }.also { instance = it }
        }
    }
}
