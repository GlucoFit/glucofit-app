package com.fitcoders.glucofitapp.view.activity.scanner

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fitcoders.glucofitapp.databinding.ActivityScannerResultBinding
import com.fitcoders.glucofitapp.service.ApiConfig
import com.fitcoders.glucofitapp.response.PostHistoryScanResponse
import com.fitcoders.glucofitapp.data.helper.ImageClassifierHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.tensorflow.lite.task.vision.classifier.Classifications
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ScannerResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerResultBinding

    companion object {
        const val IMAGE_URI = "image_uri"
        const val IMAGE_SIZE = 224
        const val OUTPUT_SIZE = 3
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
        val topResult = results[0].categories[0]
        val label = topResult.label
        val score = topResult.score

        fun Float.formatToString(): String {
            return String.format("%.2f%%", this * 100)
        }

        binding.resultText.text = "$label ${score.formatToString()}"
    }

    private fun saveHistory(imageUri: Uri, result: String) {
        if (result.isNotEmpty()) {
            val file = File(cacheDir, "image.jpg")
            contentResolver.openInputStream(imageUri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val objectName = RequestBody.create("text/plain".toMediaTypeOrNull(), result)
            val objectSugar = RequestBody.create("text/plain".toMediaTypeOrNull(), calculateSugarLevel(result).toString())

/*            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    ApiConfig.instance.postScan(body, objectName, objectSugar).enqueue(object : Callback<PostHistoryScanResponse> {
                        override fun onResponse(call: Call<PostHistoryScanResponse>, response: Response<PostHistoryScanResponse>) {
                            if (response.isSuccessful) {
                                val responseData = response.body()
                                responseData?.let {
                                    showToast("Data saved: ${it.message}")
                                    // Optionally, show more details about the saved data
                                }
                            } else {
                                showToast("Failed to save data")
                            }
                        }

                        override fun onFailure(call: Call<PostHistoryScanResponse>, t: Throwable) {
                            showToast("API call failed: ${t.message}")
                        }
                    })
                }
            }*/
        } else {
            showToast("Failed to save data!")
        }
    }

    private fun calculateSugarLevel(result: String): Int {
        // Implement your logic to calculate sugar level based on the result
        return 0 // Placeholder value
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
