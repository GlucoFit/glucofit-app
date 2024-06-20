package com.fitcoders.glucofitapp.view.activity.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityMainBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.fragment.home.HomeFragment
import com.fitcoders.glucofitapp.view.fragment.favorite.FavoriteFragment
import com.fitcoders.glucofitapp.view.fragment.history.HistoryFragment
import com.fitcoders.glucofitapp.view.fragment.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var modelfactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        modelfactory = ViewModelFactory.getInstance(this)


        setupBottomNavigation()
        val fragment = HomeFragment.newInstance()
        fragmentManager(fragment)
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
}
