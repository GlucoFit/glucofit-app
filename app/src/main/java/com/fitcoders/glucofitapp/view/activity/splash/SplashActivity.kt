package com.fitcoders.glucofitapp.view.activity.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.UserPreference
import com.fitcoders.glucofitapp.data.dataStore
import com.fitcoders.glucofitapp.view.activity.login.LoginActivity
import com.fitcoders.glucofitapp.view.activity.main.MainActivity
import com.fitcoders.glucofitapp.view.activity.onboarding.OnBoardingActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userPreference = UserPreference.getInstance(dataStore)

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                val isOnboardingComplete = userPreference.isOnboardingComplete().first()
                val isLoggedIn = userPreference.getSession().first().isLogin

                when {
                    !isOnboardingComplete -> {
                        startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
                    }
                    !isLoggedIn -> {
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    }
                    else -> {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    }
                }
                finish()
            }
        }, 2000) // 3000 milidetik = 3 det
    }
}