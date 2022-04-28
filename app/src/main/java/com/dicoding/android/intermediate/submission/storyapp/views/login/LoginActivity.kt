package com.dicoding.android.intermediate.submission.storyapp.views.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityLoginBinding
import com.dicoding.android.intermediate.submission.storyapp.views.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setHeader()
        playAnimation()
        bindLoginButton()
        bindRegisterFirstButton()
    }

    private fun setHeader() {
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.loginAsset, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleLoginPageTv, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.descLoginPageTv, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailInputLabelTv, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailInputLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordInputLabel, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordInputLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginBtn, View.ALPHA, 1f).setDuration(100)
        val register = ObjectAnimator.ofFloat(binding.registerFirstBtn, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                desc,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login,
                register
            )
            startDelay = 500
        }.start()
    }

    private fun bindLoginButton() {
        binding.loginBtn.setOnClickListener {
            // Do Nothing.
        }
    }

    private fun bindRegisterFirstButton() {
        binding.registerFirstBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}