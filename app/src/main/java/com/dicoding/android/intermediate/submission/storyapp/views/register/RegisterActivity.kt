package com.dicoding.android.intermediate.submission.storyapp.views.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.android.intermediate.submission.storyapp.R
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.android.intermediate.submission.storyapp.views.factories.UserViewModelFactory
import com.dicoding.android.intermediate.submission.storyapp.views.login.LoginActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var registerJob: Job = Job()
    private val handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var userViewModelFactory: UserViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels {
        userViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        userViewModelFactory = UserViewModelFactory.getInstance(this)
        val view = binding.root
        setContentView(view)

        setHeader()
        playAnimation()
        bindRegisterButton()
        bindBackToLoginButton()
    }

    private fun setHeader() {
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.registerAsset, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(
            binding.titleRegisterPageTv, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(
            binding.descRegisterPageTv, View.ALPHA, 1f).setDuration(100)
        val nameTextView = ObjectAnimator.ofFloat(
            binding.nameInputLabelTv, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout = ObjectAnimator.ofFloat(
            binding.nameInputLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(
            binding.emailInputLabelTv, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(
            binding.emailInputLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView = ObjectAnimator.ofFloat(
            binding.passwordInputLabel, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(
            binding.passwordInputLayout, View.ALPHA, 1f).setDuration(100)
        val registerBtn = ObjectAnimator.ofFloat(
            binding.registerBtn, View.ALPHA, 1f).setDuration(100)
        val backToLoginBtn = ObjectAnimator.ofFloat(
            binding.backToLoginBtn, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                desc,
                registerBtn,
                backToLoginBtn,
            )
            playTogether(
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
            )
            startDelay = 500
        }.start()
    }

    private fun bindRegisterButton() {
        binding.registerBtn.setOnClickListener {
            val name = binding.nameInputEditText.text.toString()
            val email = binding.emailInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()
            setLoading(true)

            lifecycleScope.launchWhenResumed {
                if (registerJob.isActive) registerJob.cancel()

                registerJob = launch {
                    registerViewModel.postRegister(name, email, password).collect{ result ->
                        result.onSuccess {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.toast_register_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            handler.postDelayed({
                                setLoading(false)
                                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            }, 1000)
                        }
                        result.onFailure {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.toast_register_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                            handler.postDelayed({
                                setLoading(false)
                            }, 1000)
                        }
                    }
                }
            }
        }
    }

    private fun bindBackToLoginButton() {
        binding.backToLoginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            nameInputEditText.isEnabled = !isLoading
            emailInputEditText.isEnabled = !isLoading
            passwordInputEditText.isEnabled = !isLoading
            registerBtn.isEnabled = !isLoading
            pbRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}