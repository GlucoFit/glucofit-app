package com.fitcoders.glucofitapp.view.activity.assessment

import HealthConditionFragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.view.activity.login.LoginActivity
import com.fitcoders.glucofitapp.view.fragment.lifestyle.LifeStyleFragment
import com.fitcoders.glucofitapp.view.fragment.userinformation.UserInformationFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AssessmentActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var nextButton: Button
    private lateinit var backButton: Button
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assessment)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        viewPager.isUserInputEnabled = false // Disable swiping between steps

        // Disable clicks on tabs
        disableTabClicks(tabs)

        nextButton = findViewById(R.id.button_next)
        backButton = findViewById(R.id.button_back)
        submitButton = findViewById(R.id.button_submit)

        nextButton.setOnClickListener {
            handleNextButtonClick()
        }

        backButton.setOnClickListener {
            moveToPreviousStep()
        }

        submitButton.setOnClickListener {
            submitAssessment()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateNavigationButtons(position)
            }
        })

        // Initialize the buttons visibility
        updateNavigationButtons(0)
    }

    private fun disableTabClicks(tabLayout: TabLayout) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Prevent tab selection
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val tabStrip = tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).setOnTouchListener { _, _ -> true }
        }
    }

    private fun handleNextButtonClick() {
        val currentFragment = supportFragmentManager.findFragmentByTag("f${viewPager.currentItem}")
        when (currentFragment) {
            is UserInformationFragment -> currentFragment.onNextButtonClicked()
            is HealthConditionFragment -> currentFragment.onNextButtonClicked()
            is LifeStyleFragment -> currentFragment.onNextButtonClicked()
            else -> moveToNextStep()
        }
    }

    fun moveToNextStep() {
        val current = viewPager.currentItem
        if (current < 2) {
            viewPager.currentItem = current + 1
        }
    }

    fun moveToPreviousStep() {
        val current = viewPager.currentItem
        if (current > 0) {
            viewPager.currentItem = current - 1
        }
    }

    private fun submitAssessment() {
        // Logika untuk submit assessment
        Toast.makeText(this, "Assessment submitted!", Toast.LENGTH_SHORT).show()
        // Arahkan ke login atau registrasi setelah asesmen selesai
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun updateNavigationButtons(position: Int) {
        when (position) {
            0 -> {
                backButton.visibility = View.GONE
                nextButton.visibility = View.VISIBLE
                submitButton.visibility = View.GONE
            }
            1 -> {
                backButton.visibility = View.VISIBLE
                nextButton.visibility = View.VISIBLE
                submitButton.visibility = View.GONE
            }
            2 -> {
                backButton.visibility = View.VISIBLE
                nextButton.visibility = View.GONE
                submitButton.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3
        )
    }
}

