package com.fitcoders.glucofitapp.view.activity.scanner

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.helper.ImageClassifierHelper
import com.fitcoders.glucofitapp.data.helper.TensorFlowHelper
import com.fitcoders.glucofitapp.response.AnalyzeResponse
import com.fitcoders.glucofitapp.response.Data
import com.fitcoders.glucofitapp.response.UplodScanResponse
import com.fitcoders.glucofitapp.utils.Event
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

// ScanViewModel.kt
class ScanViewModel(private val repository: AppRepository) : ViewModel() {

    // LiveData untuk mengamati hasil analisis makanan
    val uploadScanResponse: LiveData<UplodScanResponse?> get() = repository.uploadScanResponse
    val foodInfo: LiveData<Data?> get() = repository.foodInfo
    // LiveData untuk mengamati pesan toast (feedback error atau sukses)
    val toastText: LiveData<Event<String>> get() = repository.toastText

    // LiveData untuk mengamati status loading
    val isLoading: LiveData<Boolean> get() = repository.isLoading

    // Fungsi untuk memulai analisis gambar makanan
    fun analyzeImage(imageFile: File) {
        if (imageFile.exists() && imageFile.isFile) {
            repository.analyzeFoodImage(imageFile)
        } else {
            // Menyampaikan pesan error jika file tidak valid
            repository.toastText.postValue(Event("Invalid image file"))
        }
    }

    fun uploadScanImage(imageFile: File, objectName: String, objectSugar: String, datasetLabel: String) {
        repository.uploadScanImage(imageFile, objectName, objectSugar, datasetLabel)
    }

}
