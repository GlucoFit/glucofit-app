package com.fitcoders.glucofitapp.view.activity.scanner

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.helper.ImageClassifierHelper
import com.fitcoders.glucofitapp.response.DataFoodResponse
import org.tensorflow.lite.task.vision.classifier.Classifications

// ScanViewModel.kt
class ScanViewModel(private val repository: AppRepository) : ViewModel() {

    val classificationResult: LiveData<String> = repository.classificationResult
    val foodInfo: MutableLiveData<DataFoodResponse?> = repository.foodInfo

    // Fungsi untuk memulai klasifikasi gambar
    fun classifyImage(imageUri: Uri, context: Context) {
        val imageClassifierHelper = ImageClassifierHelper(
            context = context,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(errorMsg: String) {
                    // Tangani error
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    results?.let {
                        val firstResult = it[0].categories.firstOrNull()
                        firstResult?.let { category ->
                            repository.fetchFoodInfoByLabel(category.label)
                        }
                    }
                }
            }
        )
        imageClassifierHelper.classifyImage(imageUri)
    }
}
