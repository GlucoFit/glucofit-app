/*
package com.fitcoders.glucofitapp.view.activity.scanner

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fitcoders.glucofitapp.databinding.ActivityScannerResultBinding
import com.fitcoders.glucofitapp.data.HistoryDatabase
import com.fitcoders.glucofitapp.data.History
import com.fitcoders.glucofitapp.data.helper.ImageClassifierHelper
import com.fitcoders.glucofitapp.view.fragment.history.HistoryFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.io.FileOutputStream

class ScannerResultActivity : AppCompatActivity() {

    */
/* Initialize variable binding *//*

    private lateinit var binding: ActivityScannerResultBinding

    */
/* Initialize companion object IMAGE_URI, RESULT_TEXT, and REQUEST_HISTORY_UPDATE *//*

    companion object {
        const val IMAGE_URI = "image_url"
        const val RESULT_TEXT = "result_text"
        const val REQUEST_HISTORY_UPDATE = 1
    }

    */
/* Override the onCreate function *//*

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        */
/* Initialize the variable imageUriString with the image URI *//*

        val imageUriString = intent.getStringExtra(IMAGE_URI)
        if (imageUriString != null) {
            */
/* check if the image URI is not null, then show the image *//*

            val imageUri = Uri.parse(imageUriString)
            showImage(imageUri)

            */
/* Initialize the variable imageClassifierHelper with the ImageClassifierHelper *//*

            val imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    */
/* Implement the onError function *//*

                    override fun onError(errorMsg: String) {
                        showToast("Error")
                    }

                    */
/* Implement the onResults function *//*

                    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                        results?.let { showResults(it) }
                    }
                }
            )
            imageClassifierHelper.classifyImage(imageUri)
        } else {
            finish()
        }

        */
/* When the save button is clicked, it will save the history *//*

        binding.saveButton.setOnClickListener {
            val result = binding.resultText.text.toString()

            if (intent.getStringExtra(IMAGE_URI) == null) {
                showToast("No image URI provided")
                finish()
            } else {
                saveHistory(Uri.parse(imageUriString), result)
                showToast("Data saved")
            }
        }
    }

    */
/* Implement the onActivityResult function *//*

    private fun showImage(uri: Uri) {
        binding.resultImage.setImageURI(uri)
    }

    */
/* MoveToHistory function is used to move to the history activity *//*

    private fun moveToHistory(imageUri: Uri, result: String) {
        */
/* Create an intent to move to the history activity *//*

        val intent = Intent(this, HistoryFragment::class.java)
        intent.putExtra(RESULT_TEXT, result)
        intent.putExtra(IMAGE_URI, imageUri.toString())
        setResult(RESULT_OK, intent)
        startActivity(intent)
        finish()
    }

    */
/* SaveHistory function is used to save the history *//*

    private fun saveHistory(imageUri: Uri, result: String) {
        if (result.isNotEmpty()) {
            */
/* Save the image to the cache directory *//*

            val fileName = "cropped_image_${System.currentTimeMillis()}.jpg"
            val destinationUri = Uri.fromFile(File(cacheDir, fileName))
            contentResolver.openInputStream(imageUri)?.use { input ->
                FileOutputStream(File(cacheDir, fileName)).use { output ->
                    input.copyTo(output)
                }
            }
            val history = History(imageURL = destinationUri.toString(), result = result)
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    val database = HistoryDatabase.getDatabase(applicationContext)
                    try {
                        database.HistoryDao().insertData(history)
                        moveToHistory(destinationUri, result)
                    } catch (e: Exception) {
                        showToast("Failed to save data!")
                    }
                }
            }
        } else {
            showToast("Failed to save data!")
        }
    }

    */
/* ShowResults function is used to show the results *//*

    @SuppressLint("SetTextI18n")
    private fun showResults(results: List<Classifications>) {
        val topResult = results[0]
        val label = topResult.categories[0].label
        val score = topResult.categories[0].score

        */
/* Format the score to string *//*

        fun Float.formatToString(): String {
            return String.format("%.2f%%", this * 100)
        }
        binding.resultText.text = "$label ${score.formatToString()}"
    }

    */
/* ShowToast function is used to show the toast message *//*

    private fun showToast(message: String) {
        */
/* Show the toast message *//*

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
*/
