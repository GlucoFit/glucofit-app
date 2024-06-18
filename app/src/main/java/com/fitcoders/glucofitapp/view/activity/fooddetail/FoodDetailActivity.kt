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
import com.fitcoders.glucofitapp.response.Food
import com.fitcoders.glucofitapp.response.FoodDetails
import com.fitcoders.glucofitapp.response.FoodRecipeResponseItem
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
            finish() // Kembali ke aktivitas sebelumnya
        }
    }

    private fun displayFoodDetails() {
        // Ambil objek Food atau FoodDetails yang dikirim melalui Intent
        val food: Food? = intent.getParcelableExtra("food")
        val foodDetails: FoodDetails? = intent.getParcelableExtra("foodDetails")
        val foodRecipe: FoodRecipeResponseItem? = intent.getParcelableExtra("foodRecipe")

        // Tentukan mana yang akan ditampilkan
        when {
            food != null -> {
                // Menampilkan detail Food
                updateUIWithFood(food)
            }
            foodDetails != null -> {
                // Menampilkan detail FoodDetails
                updateUIWithFoodDetails(foodDetails)
            }
            foodRecipe != null ->{
                updateUIWithFoodRecipe(foodRecipe)
            }
            else -> {
                Toast.makeText(this, "No food details available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUIWithFood(food: Food) {
        // Muat gambar
        Glide.with(this).load(food.imageUrl).into(binding.foodImage)

        // Setel text views
        binding.foodName.text = food.recipeName
        binding.sugarContent.text = food.sugarContent.toString()
        binding.calories.text = food.calories.toString()
        binding.servings.text = food.servings.toString()

        // Tampilkan diet labels
        val dietLabels = food.dietLabels?.split(", ") ?: emptyList()
        val dietLabelsAdapter = DietLabelsAdapter(dietLabels, this)
        binding.dietLabelsRecyclerView.adapter = dietLabelsAdapter

        // Format ingredients menjadi daftar dengan bullet points
        val formattedIngredients = food.ingredients?.split(", ")?.joinToString("\n") { ingredient ->
            "• $ingredient"
        }
        binding.ingridients.text = formattedIngredients

        // Handle klik tombol "jump to instructions"
        binding.jumpToInstructions.setOnClickListener { _ ->
            // Periksa apakah URL instruksi valid
            food.instructionUrl?.let { url ->
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

    private fun updateUIWithFoodDetails(foodDetails: FoodDetails) {
        // Muat gambar
        Glide.with(this).load(foodDetails.imageUrl).into(binding.foodImage)

        // Setel text views
        binding.foodName.text = foodDetails.recipeName
        binding.sugarContent.text = foodDetails.sugarContent.toString()
        binding.calories.text = foodDetails.calories.toString()
        binding.servings.text = foodDetails.servings.toString()

        // Tampilkan diet labels
        val dietLabels = foodDetails.dietLabels?.split(", ") ?: emptyList()
        val dietLabelsAdapter = DietLabelsAdapter(dietLabels, this)
        binding.dietLabelsRecyclerView.adapter = dietLabelsAdapter

        // Format ingredients menjadi daftar dengan bullet points
        val formattedIngredients = foodDetails.ingredients?.split(", ")?.joinToString("\n") { ingredient ->
            "• $ingredient"
        }
        binding.ingridients.text = formattedIngredients

        // Handle klik tombol "jump to instructions"
        binding.jumpToInstructions.setOnClickListener { _ ->
            // Periksa apakah URL instruksi valid
            foodDetails.instructionUrl?.let { url ->
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

    private fun updateUIWithFoodRecipe(foodRecipe: FoodRecipeResponseItem) {
        // Muat gambar
        Glide.with(this).load(foodRecipe.imageUrl).into(binding.foodImage)

        // Setel text views
        binding.foodName.text = foodRecipe.recipeName
        binding.sugarContent.text = foodRecipe.sugarContent.toString()
        binding.calories.text = foodRecipe.calories.toString()
        binding.servings.text = foodRecipe.servings.toString()

        // Tampilkan diet labels
        val dietLabels = foodRecipe.dietLabels?.split(", ") ?: emptyList()
        val dietLabelsAdapter = DietLabelsAdapter(dietLabels, this)
        binding.dietLabelsRecyclerView.adapter = dietLabelsAdapter

        // Format ingredients menjadi daftar dengan bullet points
        val formattedIngredients = foodRecipe.ingredients?.split(", ")?.joinToString("\n") { ingredient ->
            "• $ingredient"
        }
        binding.ingridients.text = formattedIngredients

        // Handle klik tombol "jump to instructions"
        binding.jumpToInstructions.setOnClickListener { _ ->
            // Periksa apakah URL instruksi valid
            foodRecipe.instructionUrl?.let { url ->
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
