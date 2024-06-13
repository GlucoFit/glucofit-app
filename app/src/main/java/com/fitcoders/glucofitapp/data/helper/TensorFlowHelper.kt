package com.fitcoders.glucofitapp.data.helper

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

object TensorFlowHelper {

    private const val MODEL_FILE_NAME = "ResNet50_Model_food_classifier_model (1).tflite"
    private const val INPUT_IMAGE_SIZE = 224
    private const val FLOAT_TYPE_SIZE = 4

    private var interpreter: Interpreter? = null
    private var inputBuffer: TensorBuffer? = null
    private var outputBuffer: TensorBuffer? = null

    fun initialize(assetManager: AssetManager) {
        val modelFileDescriptor: AssetFileDescriptor = assetManager.openFd(MODEL_FILE_NAME)
        val modelInputStream = FileInputStream(modelFileDescriptor.fileDescriptor)
        val modelLength = modelFileDescriptor.length

        // Initialize TensorFlow Lite Interpreter with the model
        val modelByteBuffer = modelInputStream.channel.map(
            FileChannel.MapMode.READ_ONLY,
            modelFileDescriptor.startOffset,
            modelLength
        )

        val interpreterOptions = Interpreter.Options()
        interpreter = Interpreter(modelByteBuffer, interpreterOptions)

        // Initialize input and output tensors
        val inputShape = interpreter!!.getInputTensor(0).shape()
        inputBuffer = TensorBuffer.createFixedSize(inputShape, interpreter!!.getInputTensor(0).dataType())

        val outputShape = interpreter!!.getOutputTensor(0).shape()
        outputBuffer = TensorBuffer.createFixedSize(outputShape, interpreter!!.getOutputTensor(0).dataType())
    }


    object TensorFlowHelper {
        private var interpreter: Interpreter? = null
        private var inputBuffer: TensorBuffer? = null
        private var outputBuffer: TensorBuffer? = null

        fun initialize(context: Context) {
            val assetManager = context.assets
            val modelFileName = "ResNet50_Model_food_classifier_model (1).tflite"
            val modelDescriptor = assetManager.openFd(modelFileName)

            // Initialize TensorFlow Lite Interpreter with the model
            val modelInputStream = FileInputStream(modelDescriptor.fileDescriptor)
            val modelLength = modelDescriptor.length
            val modelByteBuffer = modelInputStream.channel.map(
                FileChannel.MapMode.READ_ONLY,
                modelDescriptor.startOffset,
                modelLength
            )

            val interpreterOptions = Interpreter.Options()
            interpreter = Interpreter(modelByteBuffer, interpreterOptions)

            // Initialize input and output tensors
            val inputShape = interpreter!!.getInputTensor(0).shape()
            inputBuffer =
                TensorBuffer.createFixedSize(inputShape, interpreter!!.getInputTensor(0).dataType())

            val outputShape = interpreter!!.getOutputTensor(0).shape()
            outputBuffer = TensorBuffer.createFixedSize(
                outputShape,
                interpreter!!.getOutputTensor(0).dataType()
            )
        }

        fun classifyImage(bitmap: Bitmap): String {
            // Preprocess the image
            val inputByteBuffer = preprocessBitmap(bitmap)

            // Run inference
            inputBuffer!!.loadBuffer(inputByteBuffer)
            interpreter!!.run(inputBuffer!!.buffer, outputBuffer!!.buffer)

            // Postprocess the inference result and get the predicted class
            val outputArray = outputBuffer!!.floatArray
            val predictedIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1

            // List of food classes in the same order as your Python list
            val foodNames = arrayOf(
                "apple_pie",
                "baby_back_ribs",
                "baklava",
                "beef_carpaccio",
                "beef_tartare",
                "beet_salad",
                "beignets",
                "bibimbap",
                "bread_pudding",
                "breakfast_burrito",
                "bruschetta",
                "caesar_salad",
                "cannoli",
                "caprese_salad",
                "carrot_cake",
                "ceviche",
                "cheesecake",
                "cheese_plate",
                "chicken_curry",
                "chicken_quesadilla",
                "chicken_wings",
                "chocolate_cake",
                "chocolate_mousse",
                "churros",
                "clam_chowder",
                "club_sandwich",
                "crab_cakes",
                "creme_brulee",
                "croque_madame",
                "cup_cakes",
                "deviled_eggs",
                "donuts",
                "dumplings",
                "edamame",
                "eggs_benedict",
                "escargots",
                "falafel",
                "filet_mignon",
                "fish_and_chips",
                "foie_gras",
                "french_fries",
                "french_onion_soup",
                "french_toast",
                "fried_calamari",
                "fried_rice",
                "frozen_yogurt",
                "garlic_bread",
                "gnocchi",
                "greek_salad",
                "grilled_cheese_sandwich",
                "grilled_salmon",
                "guacamole",
                "gyoza",
                "hamburger",
                "hot_and_sour_soup",
                "hot_dog",
                "huevos_rancheros",
                "hummus",
                "ice_cream",
                "lasagna",
                "lobster_bisque",
                "lobster_roll_sandwich",
                "macaroni_and_cheese",
                "macarons",
                "miso_soup",
                "mussels",
                "nachos",
                "omelette",
                "onion_rings",
                "oysters",
                "pad_thai",
                "paella",
                "pancakes",
                "spring_rolls",
                "steak",
                "strawberry_shortcake",
                "sushi",
                "tacos",
                "takoyaki",
                "tiramisu",
                "tuna_tartare",
                "waffles"
            )

            return foodNames[predictedIndex]

        }

        private fun preprocessBitmap(bitmap: Bitmap): ByteBuffer {
            val resizedBitmap =
                Bitmap.createScaledBitmap(bitmap, INPUT_IMAGE_SIZE, INPUT_IMAGE_SIZE, true)

            // Preprocessing steps to match ResNet50 requirements
            val inputByteBuffer =
                ByteBuffer.allocateDirect(INPUT_IMAGE_SIZE * INPUT_IMAGE_SIZE * 3 * FLOAT_TYPE_SIZE)
            inputByteBuffer.order(ByteOrder.nativeOrder())

            // Normalize the pixels values to the range of [-1, 1]
            val pixels = IntArray(INPUT_IMAGE_SIZE * INPUT_IMAGE_SIZE)
            resizedBitmap.getPixels(
                pixels,
                0,
                resizedBitmap.width,
                0,
                0,
                resizedBitmap.width,
                resizedBitmap.height
            )

            var pixel = 0
            for (i in 0 until INPUT_IMAGE_SIZE) {
                for (j in 0 until INPUT_IMAGE_SIZE) {
                    val pixelValue = pixels[pixel++]
                    inputByteBuffer.putFloat((pixelValue shr 16 and 0xFF).toFloat() / 255.0f * 2.0f - 1.0f)  // Red channel
                    inputByteBuffer.putFloat((pixelValue shr 8 and 0xFF).toFloat() / 255.0f * 2.0f - 1.0f)   // Green channel
                    inputByteBuffer.putFloat((pixelValue and 0xFF).toFloat() / 255.0f * 2.0f - 1.0f)        // Blue channel
                }
            }

            resizedBitmap.recycle()
            return inputByteBuffer
        }

        // Perform inference and classify the image
    }
}