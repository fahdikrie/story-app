package com.dicoding.android.intermediate.submission.storyapp.views.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.android.intermediate.submission.storyapp.R
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityMainBinding
import com.dicoding.android.intermediate.submission.storyapp.views.factories.UserViewModelFactory
import com.dicoding.android.intermediate.submission.storyapp.views.login.LoginActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storylist.StoryListActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storylist.StoryListActivity.Companion.EXTRA_TOKEN
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModelFactory: UserViewModelFactory
    private val mainViewModel: MainViewModel by viewModels {
        userViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        userViewModelFactory = UserViewModelFactory.getInstance(this)
        val view = binding.root
        setContentView(view)

        setHeader()
        animateLogo()
        Handler(Looper.getMainLooper()).postDelayed({
            goToNextPage()
        }, 3000)
    }

    private fun setHeader() {
        supportActionBar?.hide()
    }

    private fun animateLogo() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding
            .appLogo
            .startAnimation(fadeIn)
    }

    private fun goToNextPage() {
        lifecycleScope.launchWhenCreated {
            launch {
                mainViewModel.getUser().collect {
                    if (it.token.isEmpty()) {
                        Intent(this@MainActivity, LoginActivity::class.java).also { intent ->
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Intent(this@MainActivity, StoryListActivity::class.java).also { intent ->
                            intent.putExtra(EXTRA_TOKEN, it.token)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }
}