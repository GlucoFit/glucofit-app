package com.fitcoders.glucofitapp.view.activity.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityMainBinding
import com.fitcoders.glucofitapp.view.fragment.home.HomeFragment
import com.fitcoders.glucofitapp.view.fragment.favorite.FavoriteFragment
import com.fitcoders.glucofitapp.view.fragment.history.HistoryFragment
import com.fitcoders.glucofitapp.view.fragment.profile.ProfileFragment


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private fun fragmentManager(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.content, fragment, fragment.javaClass.simpleName)
        transaction.commit()
    }

/*    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.apply {
            bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> {
                        val fragment = HomeFragment.newInstance()
                        fragmentManager(fragment)
                    }

                    R.id.favorite -> {
                        val fragment = FavoriteFragment.newInstance("value1", "value2")
                        fragmentManager(fragment)
                    }

                    R.id.history -> {
                        val fragment = HistoryFragment.newInstance()
                        fragmentManager(fragment)
                    }

                    R.id.profile -> {
                        val fragment = ProfileFragment.newInstance()
                        fragmentManager(fragment)
                    }
                }
                bottomNavigation.menu.findItem(item.itemId)?.isChecked = true
                false
            }
        }

        val fragment = HomeFragment.newInstance()
        fragmentManager(fragment)

    }
}