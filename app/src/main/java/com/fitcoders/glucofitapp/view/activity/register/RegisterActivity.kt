package com.fitcoders.glucofitapp.view.activity.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityRegisterBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.assessment.AssessmentActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var modelFactory : ViewModelFactory
    private val  registerViewModel: RegisterViewModel by viewModels { modelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelFactory = ViewModelFactory.getInstance(this)

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.length < 8) {
                        binding.passwordEditTextLayout.error = "Password tidak boleh kurang dari 8 karakter"
                    } else {
                        binding.passwordEditTextLayout.error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed
            }
        })

        setupView()
        setupAction()
        playAnimation()

    }

    private fun setupAction() {
        binding.apply {
            loginButton.setOnClickListener {
                if (nameEditText.length() == 0 && emailEditText.length() == 0 && passwordEditText.length() < 8) {
                    nameEditText.error = getString(R.string.error_empty)
                    emailEditText.error = getString(R.string.error_empty)
                    passwordEditText.setError(getString(R.string.error_empty), null)
                } else if (nameEditText.length() != 0 && emailEditText.length() != 0 && passwordEditText.length() > 7) {
                    showLoading()
                    postText()
                    showToast()
                    moveActivity()
                }
            }
        }
    }

    private fun postText() {
        binding.apply {
            registerViewModel.postRegister(
                nameEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
    }

    private fun showToast() {
        registerViewModel.toastText.observe(this@RegisterActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this@RegisterActivity, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoading() {
        registerViewModel.isLoading.observe(this) {
            binding.pbRegister.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun moveActivity() {
        registerViewModel.registerResponse.observe(this) { response ->
            response?.let {
                val user = it.user
                if (user != null) {
                    startActivity(Intent(this, AssessmentActivity::class.java))
                    finish()
                } else {
                    // Handle the case where the user data is null or there's an error
                    Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                // Handle the case where the response is null
                Toast.makeText(this, "Registration response is null. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val logintext = ObjectAnimator.ofFloat(binding.registertext,View.ALPHA,1f).setDuration(100)
        val loginbutton  = ObjectAnimator.ofFloat(binding.registerbutton,View.ALPHA,1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup,
                logintext,
                loginbutton
            )
            startDelay = 100
        }.start()
    }
}