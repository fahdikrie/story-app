package com.dicoding.android.intermediate.submission.storyapp.views.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.dicoding.android.intermediate.submission.storyapp.R
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityMainBinding
import com.dicoding.android.intermediate.submission.storyapp.views.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setHeader()
        animateLogo()
        goToNextPage()
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
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}