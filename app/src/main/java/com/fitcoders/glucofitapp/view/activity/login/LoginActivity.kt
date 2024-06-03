package com.fitcoders.glucofitapp.view.activity.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.databinding.ActivityLoginBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.register.RegisterActivity
import com.fitcoders.glucofitapp.view.activity.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var modelFactory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { modelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        modelFactory = ViewModelFactory.getInstance(this)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerbutton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        setupView()
        playAnimation()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.apply {
            loginButton.setOnClickListener {
                if (emailEditText.length() == 0 && passwordEditText.length() < 8) {
                    emailEditText.error = getString(R.string.error_empty)
                    passwordEditText.setError(getString(R.string.error_empty), null)
                } else if (emailEditText.length() != 0 && passwordEditText.length() > 7) {
                    showLoading()
                    postText()
                    showToast()
                    moveActivity()
                    loginViewModel.login()
                }
            }
        }
    }

    private fun moveActivity() {
        loginViewModel.loginResponse.observe(this) { response ->
            response?.let {
                val user = it.user
                if (user != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Login response is null. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showToast() {
        loginViewModel.toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {
        loginViewModel.isLoading.observe(this) {
            binding.pbLogin.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun postText() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        loginViewModel.postLogin(email, password)

        loginViewModel.loginResponse.observe(this@LoginActivity) { response ->
            response?.user?.let { user ->
                val userModel = UserModel(
                    username = user.userName ?: "",
                    email = email,
                    token = AUTH_KEY + response.token,
                    isLogin = true
                )
                saveSession(userModel)
            } ?: run {
                showError("Failed to login. Please check your credentials.")
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun saveSession(session: UserModel) {
        loginViewModel.saveSession(session)
    }

    companion object {
        private const val AUTH_KEY = "Bearer "
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.desc, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val logintext = ObjectAnimator.ofFloat(binding.registertext, View.ALPHA, 1f).setDuration(100)
        val loginbutton = ObjectAnimator.ofFloat(binding.registerbutton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login,
                logintext,
                loginbutton
            )
            startDelay = 100
        }.start()
    }
}
