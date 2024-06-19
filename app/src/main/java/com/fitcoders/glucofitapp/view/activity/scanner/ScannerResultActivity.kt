package com.fitcoders.glucofitapp.view.activity.scanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.fitcoders.glucofitapp.view.activity.main.MainActivity
import com.fitcoders.glucofitapp.view.fragment.history.HistoryFragment
import java.io.File

class ScannerResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerResultBinding
    private lateinit var progressBar: ProgressBar
    private val scanViewModel: ScanViewModel by viewModels { ViewModelFactory.getInstance(application) }

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
            finish()
        }
        binding.buttonAddToHistory.setOnClickListener {
            uploadAnalysisResult()
        }
    }

    private fun uploadAnalysisResult() {
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val objectName = binding.foodName.text.toString()
        val objectSugar = binding.sugarContent.text.toString()
        val datasetLabel = "spageti" // Label dataset sesuai kebutuhan Anda

        imageUriString?.let {
            val imageUri = Uri.parse(it)
            val imageFile = File(imageUri.path ?: "")

            if (imageFile.exists() && imageFile.isFile) {
                // Menggunakan ViewModel untuk mengunggah hasil analisis ke API
                scanViewModel.uploadScanImage(imageFile, objectName, objectSugar, datasetLabel)
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

                // Menampilkan hasil di UI
                binding.foodName.text = it.foodName ?: "Unknown"
                binding.sugarContent.text = it.sugarContent ?: "No data available"
            } ?: run {
                Log.e("ScannerResultActivity", "foodInfo is null")
                finish()
            }
        })

        // Mengamati status loading
        scanViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                Log.d("ScannerResultActivity", "Loading started")
                // Tampilkan indikator loading
                progressBar.visibility = android.view.View.VISIBLE
            } else {
                Log.d("ScannerResultActivity", "Loading finished")
                // Sembunyikan indikator loading
                progressBar.visibility = android.view.View.GONE
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

        // Mengamati hasil upload scan
        scanViewModel.uploadScanResponse.observe(this, Observer { response ->
            response?.let {
                Log.d("ScannerResultActivity", "Scan uploaded: ${it.data?.objectName}, ${it.data?.objectSugar}")
                // Menampilkan pesan sukses setelah upload
                showToast("Scan uploaded successfully")
                // Pindah ke halaman riwayat setelah berhasil mengunggah
                moveToHistoryPage()
            } ?: run {
                Log.e("ScannerResultActivity", "uploadScanResponse is null")
            }
        })
    }

    private fun moveToHistoryPage() {
        // Intent untuk pindah ke halaman riwayat (sesuaikan dengan aktivitas Anda)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Tutup ScannerResultActivity setelah pindah
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
