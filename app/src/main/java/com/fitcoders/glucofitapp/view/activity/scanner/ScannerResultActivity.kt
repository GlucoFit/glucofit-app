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
import retrofit2.Callback

class ScannerResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerResultBinding
    private val scanViewModel: ScanViewModel by viewModels {
        // Buat ViewModel dengan factory
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val IMAGE_URI = "image_uri"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(IMAGE_URI)
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            showImage(imageUri)
            scanViewModel.classifyImage(imageUri, this)
        } else {
            finish()
        }

        binding.saveButton.setOnClickListener {
            val result = binding.resultText.text.toString()
            if (imageUriString != null) {
                saveHistory(Uri.parse(imageUriString), result)
            } else {
                showToast("No image URI provided")
                finish()
            }
        }

        // Observasi hasil klasifikasi
        scanViewModel.classificationResult.observe(this, Observer { label ->
            binding.resultText.text = label
        })

        // Observasi informasi makanan dari API
        scanViewModel.foodInfo.observe(this, Observer { foodInfo ->
            if (foodInfo != null) {
                binding.resultTextSugar.text = "Sugar content: ${foodInfo.sugar}g"
            } else {
                binding.resultTextSugar.text = "Failed to fetch food info"
            }
        })
    }

    private fun showImage(uri: Uri) {
        binding.resultImage.setImageURI(uri)
    }

    private fun saveHistory(imageUri: Uri, result: String) {
        // Placeholder untuk menyimpan riwayat klasifikasi
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}