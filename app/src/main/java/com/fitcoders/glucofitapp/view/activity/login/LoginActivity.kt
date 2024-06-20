package com.fitcoders.glucofitapp.view.activity.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.data.UserPreference
import com.fitcoders.glucofitapp.data.dataStore
import com.fitcoders.glucofitapp.databinding.ActivityLoginBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.assessment.AssessmentActivity
import com.fitcoders.glucofitapp.view.activity.register.RegisterActivity
import com.fitcoders.glucofitapp.view.activity.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var modelFactory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { modelFactory }
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        modelFactory = ViewModelFactory.getInstance(this)
        userPreference = UserPreference.getInstance(dataStore)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerbutton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        setupView()
        playAnimation()
        setupAction()
       // moveActivity()

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

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak ada aksi yang diperlukan
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                        binding.emailEditTextLayout.error = "Email tidak valid"
                    } else {
                        binding.emailEditTextLayout.error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Tidak ada aksi yang diperlukan
            }
        })


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
                   /* observeLoginResponse()
                    observeAssessmentStatus()*/

                }
            }
        }
    }



    private fun moveActivity() {
        loginViewModel.loginResponse.observe(this) { response ->
            response?.let {loginResponse ->
                val user = loginResponse.user
                val token = loginResponse.token
                if (user != null && token != null) {
                    lifecycleScope.launch {
                        loginViewModel.saveSession(
                            UserModel(
                                username = user.userName ?: "",
                                email = user.email ?: "",
                                token = token, // Simpan token
                                isLogin = true
                            )
                        )

                        delay(500)

                        loginViewModel.checkAssessmentStatus()

                          Log.d("login","apakah pidah ??,username:${user.userName} ,TOKEN :${token}")

                        loginViewModel.assessmentStatus.observe(this@LoginActivity) {hasAssessment ->
                            if (hasAssessment == true) {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            } else {
                                startActivity(Intent(this@LoginActivity, AssessmentActivity::class.java))
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Login gagal. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Respon login kosong. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

/*    private fun moveActivity() {
        loginViewModel.loginResponse.observe(this) { response ->
            response?.let { loginResponse ->
                val user = loginResponse.user
                val token = loginResponse.token
                if (user != null && token != null) {
                    lifecycleScope.launch {
                        loginViewModel.saveSession(
                            UserModel(
                               *//* username = user.userName ?: "",
                                email = user.email ?: "",*//*
                                token = token, // Simpan token
                                isLogin = true
                            )
                        )

                        // Langsung pindah ke MainActivity tanpa pengecekan assessment status
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Login gagal. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Respon login kosong. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }
    }*/

   /* private fun observeLoginResponse() {
        // Observe the login response
        loginViewModel.loginResponse.observe(this) { response ->
            response?.let { loginResponse ->
                val user = loginResponse.user
                val token = loginResponse.token

                if (user != null && !token.isNullOrEmpty()) {
                    lifecycleScope.launch {
                        // Save the session with token
                        loginViewModel.saveSession(
                            UserModel(
                                username = user.userName ?: "",
                                email = user.email ?: "",
                                token = token, // Save token
                                isLogin = true
                            )
                        )

                        delay(500)  // Ensure the token is stored

                        // Check the assessment status if the token is valid
                        loginViewModel.checkAssessmentStatus()

                    }
                } else {
                    Toast.makeText(this, "Login gagal. Token tidak valid.", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Respon login kosong. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeAssessmentStatus() {
        loginViewModel.assessmentStatus.observe(this) { hasAssessment ->
            if (hasAssessment != null) {
                val intent = if (hasAssessment) {
                    Intent(this@LoginActivity, MainActivity::class.java)
                } else {
                    Intent(this@LoginActivity, AssessmentActivity::class.java)
                }
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }
    }
*/

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
