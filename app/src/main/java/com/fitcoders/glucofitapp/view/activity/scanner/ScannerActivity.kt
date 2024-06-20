package com.fitcoders.glucofitapp.view.activity.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityScannerBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.main.MainActivity
import com.fitcoders.glucofitapp.view.activity.scanner.ScannerResultActivity.Companion.EXTRA_IMAGE_URI
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.time.delay
import java.io.File
import java.util.logging.Handler

class ScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerBinding
    private var initImageUri: Uri? = null
    private var croppedImageUri: Uri? = null
    private lateinit var photoUri: Uri
    private lateinit var previewImageView: ImageView
    private lateinit var modelFactory: ViewModelFactory
    private val scanViewModel: ScanViewModel by viewModels { modelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        previewImageView = findViewById(R.id.previewImageView)

        // Inisialisasi ViewModelFactory
        modelFactory = ViewModelFactory.getInstance(this)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        val titleText: TextView = findViewById(R.id.titleText)
        val backButton: ImageButton = findViewById(R.id.backButton)

        titleText.text = getString(R.string.scanner_title)
        backButton.visibility = ImageButton.VISIBLE

        backButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupListeners() {
        binding.galleryButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, null)
            launcherIntentGallery.launch(chooser)
        }

        binding.cameraButton.setOnClickListener {
            if (checkCameraPermission()) {
                launchCamera()
            } else {
                requestCameraPermission()
            }
        }

        binding.analyzeButton.setOnClickListener {
            Log.d("ScannerActivity", "Analyze button clicked")
            croppedImageUri?.let { uri ->
                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, ScannerResultActivity::class.java).apply {
                        putExtra(EXTRA_IMAGE_URI, uri.toString())
                    }
                    startActivity(intent)
                }, 300) // Delay 300 ms
            }
        }
    }

    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = cacheDir
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    private fun uCropUtils(sourceUri: Uri) {
        val maxWidth = 4000
        val maxHeight = 4000
        val fileName = "${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, fileName))
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(2f, 2f)
            .withMaxResultSize(maxWidth, maxHeight)
            .start(this)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data
            selectedImg?.let { uri ->
                initImageUri = uri
                showImage()
                uCropUtils(uri)
            } ?: showToast(getString(R.string.image_get_uri_error))
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            initImageUri = photoUri
            showImage()
            uCropUtils(photoUri)
        } else {
            showToast(getString(R.string.image_capture_error))
        }
    }

    private fun launchCamera() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            photoFile
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        launcherIntentCamera.launch(intent)
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                launchCamera()
            } else {
                showToast("Camera permission is required to use the camera.")
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                showCroppedImage(it)
            } ?: showToast(getString(R.string.image_crop_error))
        } else if (resultCode == UCrop.RESULT_ERROR) {
            showToast("Crop error: ${UCrop.getError(data!!)?.message}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showCroppedImage(uri: Uri) {
        previewImageView.setImageURI(uri)
        croppedImageUri = uri
    }

    private fun showImage() {
        initImageUri?.let { uri ->
            previewImageView.setImageURI(uri)
        } ?: showToast(getString(R.string.image_classifier_failed))
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }
}
