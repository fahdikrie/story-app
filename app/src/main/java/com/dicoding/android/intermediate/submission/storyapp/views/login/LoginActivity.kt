package com.dicoding.android.intermediate.submission.storyapp.views.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityLoginBinding
import com.dicoding.android.intermediate.submission.storyapp.models.responses.LoginResponse
import com.dicoding.android.intermediate.submission.storyapp.models.sessions.UserModel
import com.dicoding.android.intermediate.submission.storyapp.views.factories.UserViewModelFactory
import com.dicoding.android.intermediate.submission.storyapp.views.register.RegisterActivity
import com.dicoding.android.intermediate.submission.storyapp.views.register.RegisterViewModel
import com.dicoding.android.intermediate.submission.storyapp.views.storydetail.StoryDetailActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storylist.StoryListActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var loginJob: Job = Job()
    private val handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var userViewModelFactory: UserViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels {
        userViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        userViewModelFactory = UserViewModelFactory.getInstance(this)
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
            val email = binding.emailInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()
            setLoading(true)

            lifecycleScope.launchWhenResumed {
                if (loginJob.isActive) loginJob.cancel()

                loginJob = launch {
                    loginViewModel.postLogin(email, password).collect{ result ->
                        result.onSuccess {
                            Toast.makeText(
                                applicationContext,
                                "User successfully logged in!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val response = it as LoginResponse
                            val (name, _, token) = response.loginResult
                            loginViewModel.saveUser(UserModel(name, email, token))

                            handler.postDelayed({
                                setLoading(false)
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        StoryListActivity::class.java
                                    )
                                )
                            }, 1000)
                        }
                        result.onFailure {
                            Toast.makeText(
                                applicationContext,
                                "Error when logging in user!",
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

    private fun bindRegisterFirstButton() {
        binding.registerFirstBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            emailInputEditText.isEnabled = !isLoading
            passwordInputEditText.isEnabled = !isLoading
            loginBtn.isEnabled = !isLoading
            pbLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}