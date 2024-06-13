package com.fitcoders.glucofitapp.data.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.IOException

class ImageClassifierHelper(
    val threshold: Float = 0.2f,
    var maxResults: Int = 3,
    val modelName: String = "mobilenet_v1.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    interface ClassifierListener {
        fun onError(errorMsg: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }

    init {
        setupImageClassifier()
    }

    fun classifyImage(imageUri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        // Konversi URI ke Bitmap
        val bitmap = uriToBitmap(imageUri)

        // Buat TensorImage dari Bitmap
        val tensorImage = TensorImage.fromBitmap(bitmap)

        // Proses gambar: resize dan normalisasi
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(NormalizeOp(0.0f, 1.0f)) // Normalisasi nilai piksel antara 0 dan 1
            .build()

        val processedImage = imageProcessor.process(tensorImage)

        // Jalankan inferensi
        var inferenceTime = SystemClock.uptimeMillis()
        val results = imageClassifier?.classify(processedImage)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime

        if (results.isNullOrEmpty()) {
            classifierListener?.onError("Classification returned no results.")
        } else {
            classifierListener?.onResults(results, inferenceTime)
        }
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IOException) {
            val errorMessage = "Failed to load model: ${e.message}"
            classifierListener?.onError(errorMessage)
            Log.e(TAG, errorMessage)
        }
    }

    private fun uriToBitmap(imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}
