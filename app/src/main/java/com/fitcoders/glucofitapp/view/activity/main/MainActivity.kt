package com.fitcoders.glucofitapp.view.activity.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.UserPreference
import com.fitcoders.glucofitapp.data.dataStore
import com.fitcoders.glucofitapp.databinding.ActivityMainBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.login.LoginActivity
import com.fitcoders.glucofitapp.view.activity.onboarding.OnBoardingActivity
import com.fitcoders.glucofitapp.view.fragment.home.HomeFragment
import com.fitcoders.glucofitapp.view.fragment.favorite.FavoriteFragment
import com.fitcoders.glucofitapp.view.fragment.history.HistoryFragment
import com.fitcoders.glucofitapp.view.fragment.profile.ProfileFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private var token = ""
    private lateinit var modelfactory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { modelfactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        modelfactory = ViewModelFactory.getInstance(this)

        setupUser()
    }

    private fun setupUser() {
        mainViewModel.getSession().observe(this) { session ->
            token = session.token
            if (!session.isLogin) {
                navigateToLogin()
            } else {
                setupBottomNavigation()
                val fragment = HomeFragment.newInstance()
                fragmentManager(fragment)
            }
        }
        showToast()
    }

    private fun fragmentManager(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, fragment, fragment.javaClass.simpleName)
        transaction.commit()
    }

    private fun setupBottomNavigation() {
        mainBinding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.home -> HomeFragment.newInstance()
                R.id.favorite -> FavoriteFragment.newInstance()
                R.id.history -> HistoryFragment.newInstance()
                R.id.profile -> ProfileFragment.newInstance()
                else -> return@setOnItemSelectedListener false
            }
            fragmentManager(fragment)
            true
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast() {
        mainViewModel.toastText.observe(this) { event ->
            event.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun finish() {
        super.finish()
        finishAffinity()
    }
}
