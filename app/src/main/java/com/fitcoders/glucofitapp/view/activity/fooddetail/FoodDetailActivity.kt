package com.fitcoders.glucofitapp.view.activity.fooddetail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityFoodDetailBinding
import com.fitcoders.glucofitapp.response.FoodDetails
import com.fitcoders.glucofitapp.utils.adapter.DietLabelsAdapter
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
        displayFoodDetails()
    }

    private fun setupUI() {
        val titleText: TextView = findViewById(R.id.titleText)
        val backButton: ImageButton = findViewById(R.id.backButton)

        titleText.text = getString(R.string.food_detail_title)
        backButton.visibility = ImageButton.VISIBLE

        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }
    }

    private fun displayFoodDetails() {
        // Get the FoodDetails object passed from the previous activity
        val foodDetails: FoodDetails? = intent.getParcelableExtra("foodDetails")

        // Update the UI with the food details
        foodDetails?.let {
            // Load the image
            Glide.with(this).load(it.imageUrl).into(binding.foodImage)

            // Set text views
            binding.foodName.text = it.recipeName
            binding.sugarContent.text = "${it.sugarContent}"
            binding.calories.text = "${it.calories}"
            //binding.ingridientsTitle.text = it.ingredients
           // binding.dietLabelsTitle.text = it.dietLabels

            // Display diet labels
            val dietLabels = it.dietLabels?.split(", ") ?: emptyList()
            val dietLabelsAdapter = DietLabelsAdapter(dietLabels, this)
            binding.dietLabelsRecyclerView.adapter = dietLabelsAdapter

            // Format ingredients into a list with bullet points
            val formattedIngredients = it.ingredients?.split(", ")?.joinToString("\n") { ingredient ->
                "• $ingredient"
            }
            binding.ingridients.text = formattedIngredients

            binding.jumpToInstructions.setOnClickListener { _ ->
                // Check if the instruction URL is valid
                it.instructionUrl?.let { url ->
                    if (url.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(url)
                        }
                        try {
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(this, "No browser found to open the URL", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Instruction URL is empty", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(this, "No instruction URL available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}