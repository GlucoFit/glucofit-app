package com.fitcoders.glucofitapp.data.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.fitcoders.glucofitapp.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.lang.IllegalStateException


/* ImageClassifierHelper is a class that is used to classify an image */
class ImageClassifierHelper(
    /* threshold is a variable that is used to set the threshold value, maxResults is a variable that is used to set the maximum number of results, modelName is a variable that is used to set the model name, context is a variable that is used to get the context, and classifierListener is a variable that is used to get the ClassifierListener object */
    val threshold: Float = 0.2f,
    var maxResults: Int = 3,
    val modelName: String = "model_buat_metadata.tflite",
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
        /* setupImageClassifier() is a function that is used to set up the image classifier */
        setupImageClassifier()
    }

    /* classifyImage() is a function that is used to classify the image */
    fun classifyImage(imageUri: Uri) {
        /* imageProcessor is a variable that is used to process the image, tensorImage is a variable that is used to hold the image data, and inferenceTime is a variable that is used to hold the inference time */
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        /* imageProcessor is used to process the image */
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.UINT8))
            .build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(uriToBitmap(imageUri)))

        var inferenceTime = SystemClock.uptimeMillis()
        val results = imageClassifier?.classify(tensorImage)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime
        classifierListener?.onResults(
            results,
            inferenceTime
        )
    }

    /* setupImageClassifier() is a function that is used to set up the image classifier */
    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionBuilder.build())

        /* Error handling for the image classifier */
        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAGIMAGE, e.message.toString())
        }

    }

    /* uriToBitmap() is a function that is used to convert the image URI to a bitmap */
    private fun uriToBitmap(imageUri: Uri): Bitmap {
        /* source is a variable that is used to create a source from the image URI */
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)
    }

    companion object {
        private const val TAGIMAGE = "ImageClassifierHelperModified" //
    }
}
