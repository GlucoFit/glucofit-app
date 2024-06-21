package com.fitcoders.glucofitapp.view.activity.scanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityScannerResultBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import java.io.File
import java.util.Locale

class ScannerResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerResultBinding
    private lateinit var progressBar: ProgressBar
    private val scanViewModel: ScanViewModel by viewModels { ViewModelFactory.getInstance(application) }

    private var originalSugarContent: String? = null // Store the original sugar content value

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressBar = findViewById(R.id.progressBar)

        setupUI()
        startAnalysis()
        setupObservers()
    }

    private fun setupUI() {
        val titleText: TextView = findViewById(R.id.titleText)
        val backButton: ImageButton = findViewById(R.id.backButton)

        titleText.text = getString(R.string.scanner_result_title)
        backButton.visibility = ImageButton.VISIBLE

        backButton.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.buttonAddToHistory.setOnClickListener {
            uploadAnalysisResult()
        }
    }

    private fun uploadAnalysisResult() {
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val objectName = binding.foodName.text.toString()
        val objectSugar = originalSugarContent.toString() // Use the original sugar content value
        val datasetLabel = "spageti" // Label dataset sesuai kebutuhan Anda

        imageUriString?.let {
            val imageUri = Uri.parse(it)
            val imageFile = File(imageUri.path ?: "")

            if (imageFile.exists() && imageFile.isFile) {
                // Menggunakan ViewModel untuk mengunggah hasil analisis ke API
                scanViewModel.uploadScanImage(imageFile, objectName, objectSugar, datasetLabel)
                moveToHistoryPageWithDelay()
                showToast("Scan uploaded successfully")
            } else {
                showToast(getString(R.string.invalid_image_file))
            }
        }

    }

    private fun startAnalysis() {
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val imageUri = imageUriString?.let { Uri.parse(it) }

        imageUri?.let {
            // Menampilkan gambar yang dianalisis
            binding.previewImageView.setImageURI(it)

            // Mulai analisis gambar
            val imageFile = File(it.path ?: "")
            if (imageFile.exists() && imageFile.isFile) {
                Log.d("ScannerResultActivity", "Starting analysis for image: $it")
                // Tampilkan progress bar
                progressBar.visibility = android.view.View.VISIBLE
                // Menggunakan ViewModel untuk mengirim gambar ke API
                scanViewModel.analyzeImage(imageFile)
            } else {
                showToast(getString(R.string.invalid_image_file))
                finish()
            }
        } ?: run {
            showToast("No image URI provided")
            finish()
        }
    }

    private fun setupObservers() {
        // Mengamati hasil analisis makanan
        scanViewModel.foodInfo.observe(this, Observer { foodInfo ->
            foodInfo?.let {
                Log.d("ScannerResultActivity", "Food info received: ${it.foodName}, ${it.sugarContent}")
                // Sembunyikan progress bar setelah analisis selesai
                progressBar.visibility = android.view.View.GONE

                // Store the original sugar content value
                originalSugarContent = it.sugarContent

                // Format and display food name and sugar content
                binding.foodName.text = formatFoodName(it.foodName ?: "Unknown")
                binding.sugarContent.text = if (it.sugarContent != null) "${it.sugarContent} Gram" else "Unknown"
            } ?: run {
                Log.e("ScannerResultActivity", "foodInfo is null")
                finish()
            }
        })

        // Mengamati pesan toast (error atau sukses)
        scanViewModel.toastText.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { message ->
                showToast(message)
                Log.e("ScannerResultActivity", "Toast message: $message")
                // Sembunyikan progress bar jika ada pesan toast (error)
                progressBar.visibility = android.view.View.GONE
            }
        })
    }

    private fun moveToHistoryPageWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
            Log.d("Pindah", "berhasil")
        }, 200) // Delay 500 ms
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun formatFoodName(foodName: String): String {
        return foodName.split("_").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }
}
