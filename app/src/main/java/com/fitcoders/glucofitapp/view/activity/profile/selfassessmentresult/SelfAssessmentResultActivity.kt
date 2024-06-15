package com.fitcoders.glucofitapp.view.activity.profile.selfassessmentresult

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivitySelfAssessmentResultBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory

class SelfAssessmentResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelfAssessmentResultBinding
    private lateinit var modelFactory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelfAssessmentResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelFactory = ViewModelFactory.getInstance(this)

        setupUI()
    }

    private fun setupUI() {
        val titleText: TextView = findViewById(R.id.titleText)
        val backButton: ImageButton = findViewById(R.id.backButton)

        titleText.text = "Self Assessment Result"
        backButton.visibility = ImageButton.VISIBLE

        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }

    }
}