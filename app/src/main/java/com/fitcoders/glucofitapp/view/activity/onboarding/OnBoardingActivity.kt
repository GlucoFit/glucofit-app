package com.fitcoders.glucofitapp.view.activity.onboarding

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.UserPreference
import com.fitcoders.glucofitapp.data.dataStore
import com.fitcoders.glucofitapp.databinding.ActivityOnBorardingBinding
import com.fitcoders.glucofitapp.view.activity.assessment.AssessmentActivity
import com.fitcoders.glucofitapp.view.activity.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBorardingBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreference = UserPreference.getInstance(dataStore)

        lifecycleScope.launch {
            if (userPreference.isOnboardingComplete().first()) {
                startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
                finish()
                return@launch
            }
        }

        binding = ActivityOnBorardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        binding.button.setOnClickListener {
            lifecycleScope.launch {
                userPreference.setOnboardingComplete()
                startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
                finish()
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
}

