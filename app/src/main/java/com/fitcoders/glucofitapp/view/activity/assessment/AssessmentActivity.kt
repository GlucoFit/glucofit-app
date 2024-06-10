package com.fitcoders.glucofitapp.view.activity.assessment

import HealthConditionFragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.UserPreference
import com.fitcoders.glucofitapp.data.dataStore
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.main.MainActivity
import com.fitcoders.glucofitapp.view.fragment.lifestyle.LifeStyleFragment
import com.fitcoders.glucofitapp.view.fragment.userinformation.UserInformationFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class AssessmentActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var nextButton: Button
    private lateinit var backButton: Button
    private lateinit var submitButton: Button
    private lateinit var userPreference: UserPreference
    private lateinit var assessmentViewModel: AssessmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assessment)

        val factory = ViewModelFactory.getInstance(this)
        assessmentViewModel = ViewModelProvider(this, factory).get(AssessmentViewModel::class.java)

        userPreference = UserPreference.getInstance(dataStore)

        lifecycleScope.launch {
            userPreference.setInAssessment(true)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        viewPager.isUserInputEnabled = false

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
            if (collectAllData()) {
                submitAssessment()
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateNavigationButtons(position)
            }
        })

        updateNavigationButtons(0)
    }

    private fun disableTabClicks(tabLayout: TabLayout) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
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
        val isInputValid = when (currentFragment) {
            is UserInformationFragment -> currentFragment.collectData()
            is HealthConditionFragment -> currentFragment.collectData()
            is LifeStyleFragment -> currentFragment.collectData()
            else -> true
        }

        if (isInputValid) {
            moveToNextStep()
        }
    }

    fun moveToNextStep() {
        val current = viewPager.currentItem
        if (current < 2) {
            viewPager.currentItem = current + 1
        }
        updateSubmitButtonState()
    }

    fun moveToPreviousStep() {
        val current = viewPager.currentItem
        if (current > 0) {
            viewPager.currentItem = current - 1
        }
        updateSubmitButtonState()
    }

    fun collectAllData(): Boolean {
        val currentFragment = supportFragmentManager.findFragmentByTag("f${viewPager.currentItem}")
        return when (currentFragment) {
            is UserInformationFragment -> currentFragment.collectData()
            is HealthConditionFragment -> currentFragment.collectData()
            is LifeStyleFragment -> currentFragment.collectData()
            else -> true
        }
    }

    fun submitAssessment() {
        assessmentViewModel.submitAssessment()
        Toast.makeText(this, "Assessment submitted!", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            userPreference.setAssessmentComplete()
            startActivity(Intent(this@AssessmentActivity, MainActivity::class.java))
            finish()
        }
    }

    fun updateSubmitButtonState() {
        val currentFragment = supportFragmentManager.findFragmentByTag("f${viewPager.currentItem}")
        val isInputValid = when (currentFragment) {
            is UserInformationFragment -> currentFragment.validateInputs()
            is LifeStyleFragment -> currentFragment.validateInputs()
            is HealthConditionFragment -> currentFragment.validateInputs()
            else -> false
        }

        submitButton.isEnabled = isInputValid
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
                updateSubmitButtonState() // Ensure the submit button is correctly enabled/disabled
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
