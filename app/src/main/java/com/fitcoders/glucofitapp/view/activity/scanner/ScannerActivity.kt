package com.fitcoders.glucofitapp.view.activity.scanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityScannerBinding
import com.fitcoders.glucofitapp.view.activity.main.MainActivity
import com.fitcoders.glucofitapp.view.activity.register.RegisterActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalantis.ucrop.UCrop
import java.io.File

class ScannerActivity : AppCompatActivity() {
    /* Initialize variable binding, initImageUri, croppedImageUri, and bottomNavigationBar */
    private lateinit var binding: ActivityScannerBinding
    private var initImageUri: Uri? = null
    private var croppedImageUri: Uri? = null

    /* Override the onCreate function */
    override fun onCreate(savedInstanceState: Bundle?) {
        /* Call the onCreate function from the parent class */
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titleText: TextView = findViewById(R.id.titleText)
        val backButton: ImageButton = findViewById(R.id.backButton)

        titleText.text = "Scanner"

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.galleryButton.setOnClickListener {
            /* Create an intent to get the image from the gallery */
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, null)
            launcherIntentGallery.launch(chooser)
        }

        binding.cameraButton.setOnClickListener {

        }

/*        binding.analyzeButton.setOnClickListener {
            *//* When the analyze button is clicked, it will navigate to the ResultActivity *//*
            initImageUri?.let {
                val intent = Intent(this, ScannerResultActivity::class.java)
                croppedImageUri?.let { uri ->
                    intent.putExtra(ScannerResultActivity.IMAGE_URI, uri.toString())
                } ?: showToast(getString(R.string.image_classifier_failed))

                val resultIntent = Intent(this, ScannerResultActivity::class.java)
                croppedImageUri?.let { uri ->
                    resultIntent.putExtra(ScannerResultActivity.IMAGE_URI, uri.toString())
                    startActivity(resultIntent)
                } ?: showToast(getString(R.string.image_classifier_failed))
            } ?: run {
                showToast(getString(R.string.image_classifier_failed))
            }
        }*/
    }

    /* uCropUtils() is a function that is used to crop the image */
    private fun uCropUtils(sourceUri: Uri) {
        val maxWidth = 3000
        val maxHeight = 3000
        val fileName = "${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, fileName))
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(2f, 2f)
            .withMaxResultSize(maxWidth, maxHeight)
            .start(this)
    }

    /* launcherIntentGallery is a variable that is used to register the activity result */
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            /* Check if the result is OK */
            val selectedImg = result.data?.data
            selectedImg?.let { uri ->
                initImageUri = uri
                showImage()
                uCropUtils(uri)
            } ?: showToast("There is a problem to get URI form the image")
        }
    }

    /* onActivityResult is a function that is used to handle the result from the activity */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            /* Check if the requestCode is UCrop.REQUEST_CROP and the resultCode is OK */
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                showCroppedImage(resultUri)
            } ?: showToast("Error to crop the image, please try again!")
        } else if (resultCode == UCrop.RESULT_ERROR) {
            /* Check if the resultCode is UCrop.RESULT_ERROR */
            showToast("Crop error: ${UCrop.getError(data!!)?.message}")
        }
    }

    /* showToast is a function that is used to show the toast message */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /* showCroppedImage is a function that is used to show the cropped image */
    private fun showCroppedImage(uri: Uri) {
        binding.previewImageView.setImageURI(uri)
        croppedImageUri = uri
    }

    /* showImage is a function that is used to show the image */
    private fun showImage() {
        initImageUri?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        } /*?: showToast(getString(R.string.image_classifier_failed))*/
    }
}