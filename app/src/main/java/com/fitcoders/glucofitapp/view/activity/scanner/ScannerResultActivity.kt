package com.fitcoders.glucofitapp.view.activity.scanner

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fitcoders.glucofitapp.databinding.ActivityScannerResultBinding
import com.fitcoders.glucofitapp.data.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class ScannerResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerResultBinding

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
    }

    private fun showImage(uri: Uri) {
        binding.resultImage.setImageURI(uri)
    }

    @SuppressLint("SetTextI18n")
    private fun showResults(results: List<Classifications>) {
        // Ambil kategori dengan probabilitas tertinggi dari hasil klasifikasi
        val topResult = results[0].categories.maxByOrNull { it.score }
        topResult?.let {
            val label = it.label
            val score = it.score

            // Tampilkan hanya label dari hasil klasifikasi
            binding.resultText.text = label
        }
    }

    private fun saveHistory(imageUri: Uri, result: String) {
        // Placeholder untuk menyimpan riwayat klasifikasi
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}