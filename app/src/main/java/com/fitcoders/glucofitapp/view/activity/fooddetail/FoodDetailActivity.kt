package com.fitcoders.glucofitapp.view.activity.fooddetail

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityFoodDetailBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.main.MainActivity

class FoodDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var modelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelFactory = ViewModelFactory.getInstance(this)
        setupUI()
    }

    private fun setupUI() {
        val titleText: TextView = findViewById(R.id.titleText)
        val backButton: ImageButton = findViewById(R.id.backButton)

        titleText.text = getString(R.string.food_detail_title)
        backButton.visibility = ImageButton.VISIBLE

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}