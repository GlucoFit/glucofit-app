package com.fitcoders.glucofitapp.view.activity.scanner

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.helper.ImageClassifierHelper
import com.fitcoders.glucofitapp.data.helper.TensorFlowHelper
import com.fitcoders.glucofitapp.response.DataFoodResponse
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications

// ScanViewModel.kt
class ScanViewModel(private val repository: AppRepository) : ViewModel() {


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
   /* fun classifyImage(imageUri: Uri, context: Context) {
        // Load the bitmap from the image URI
        val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri))

        // Initialize TensorFlowHelper if not already initialized
        TensorFlowHelper.initialize(context.assets)

        // Classify the image and get the result
        val label = TensorFlowHelper.classifyImage(bitmap)

        // Fetch additional information based on the classification result
        repository.fetchFoodInfoByLabel(label)
    }*/


}
