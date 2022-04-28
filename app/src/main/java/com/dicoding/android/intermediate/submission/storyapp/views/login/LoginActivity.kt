package com.dicoding.android.intermediate.submission.storyapp.views.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setHeader()
    }

    private fun setHeader() {
        supportActionBar?.hide()
    }
}