package com.fitcoders.glucofitapp.view.activity.scanner

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.fitcoders.glucofitapp.databinding.ActivityScannerResultBinding
import com.fitcoders.glucofitapp.data.helper.ImageClassifierHelper
import com.fitcoders.glucofitapp.service.ApiConfig
import com.fitcoders.glucofitapp.view.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications

class ScannerResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerResultBinding
    private val scanViewModel: ScanViewModel by viewModels {
        // Initialize the ViewModel using a factory method
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val IMAGE_URI = "image_uri"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the image URI from the intent extras
        val imageUriString = intent.getStringExtra(IMAGE_URI)

        imageUriString?.let {
            val imageUri = Uri.parse(it)
            showImage(imageUri)

            // Using ViewModel to classify the image
            scanViewModel.classifyImage(imageUri, this)

            // Using ImageClassifierHelper to classify the image
            val imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(errorMsg: String) {
                        showToast("Error: $errorMsg")
                    }

                    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                        results?.let { showResults(it) }
                    }
                }
            )
            imageClassifierHelper.classifyImage(imageUri)
        } ?: run {
            showToast("No image URI provided")
            finish()
        }

        // Save button click listener to save history
        binding.saveButton.setOnClickListener {
            val result = binding.resultText.text.toString()
            imageUriString?.let {
                saveHistory(Uri.parse(it), result)
            } ?: run {
                showToast("No image URI provided")
                finish()
            }
        }

        // Observe food information from the API and display sugar data
        scanViewModel.foodInfo.observe(this, Observer { foodInfo ->
            if (foodInfo != null && foodInfo.sugar != null) {
                val sugarItems = foodInfo.sugar // List of SugarItem?
                // Filter not null items and extract sugar values
                val sugarValues = sugarItems.filterNotNull().mapNotNull { it.sugar }
                val sugarInfo = sugarValues.joinToString(separator = ", ") { "${it}g" } // Join sugar values with commas
                binding.resultTextSugar.text = "Sugar content: $sugarInfo" // Display sugar content
            } else {
                binding.resultTextSugar.text = "No sugar data available or failed to fetch food info"
            }
        })

    }

 /*   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(IMAGE_URI)
        imageUriString?.let {
            val imageUri = Uri.parse(it)
            showImage(imageUri)

            scanViewModel.classifyImage(imageUri, this)
        } ?: run {
            showToast("No image URI provided")
            finish()
        }

        scanViewModel.foodInfo.observe(this, Observer { foodInfo ->
            if (foodInfo != null && foodInfo.sugar != null) {
                val sugarItems = foodInfo.sugar // List of SugarItem?
                // Filter not null items and extract sugar values
                val sugarValues = sugarItems.filterNotNull().mapNotNull { it.sugar }
                val sugarInfo = sugarValues.joinToString(separator = ", ") { "${it}g" } // Join sugar values with commas
                binding.resultTextSugar.text = "Sugar content: $sugarInfo" // Display sugar content
            } else {
                binding.resultTextSugar.text = "No sugar data available or failed to fetch food info"
            }
        })
    }*/
    @SuppressLint("SetTextI18n")
    private fun showResults(results: List<Classifications>) {
        if (results.isNotEmpty() && results[0].categories.isNotEmpty()) {
            val topResult = results[0].categories[0]
            val label = topResult.label
            val score = topResult.score.formatToString()

            binding.resultText.text = "$label ${score}"
        } else {
            binding.resultText.text = "No classification results available"
        }
    }

    private fun showImage(uri: Uri) {
        binding.resultImage.setImageURI(uri)
    }

    private fun saveHistory(imageUri: Uri, result: String) {
        // Placeholder for saving the classification history
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Extension function to format score as a percentage string
    private fun Float.formatToString(): String {
        return String.format("%.2f%%", this * 100)
    }
}
