package com.fitcoders.glucofitapp.view.activity.profile.password

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityAccountBinding
import com.fitcoders.glucofitapp.databinding.ActivityPasswordBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.profile.account.AccountViewModel

class PasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordBinding
    private lateinit var modelFactory: ViewModelFactory
    private lateinit var passwordViewModel: PasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelFactory = ViewModelFactory.getInstance(this)
        passwordViewModel = ViewModelProvider(this, modelFactory).get(PasswordViewModel::class.java)

        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        val titleText: TextView = findViewById(R.id.titleText)
        val backButton: ImageButton = findViewById(R.id.backButton)

        titleText.text = "Password"
        backButton.visibility = ImageButton.VISIBLE

        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }

        // Mengatur click listener untuk tombol change password
        binding.changePasswordButton.setOnClickListener {
            val currentPassword = binding.passwordEditText.text.toString().trim()
            val newPassword = binding.newPasswordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            // Validasi untuk memastikan bahwa semua bidang diisi
            if (currentPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                // Validasi untuk memastikan bahwa newPassword dan confirmPassword sesuai
                if (newPassword == confirmPassword) {
                    passwordViewModel.updatePassword(currentPassword, newPassword)
                } else {
                    Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        // Mengamati perubahan dalam update password response
        passwordViewModel.updatePasswordResponse.observe(this, Observer { response ->
            if (response != null) {
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                binding.passwordEditText.text?.clear() // Mengosongkan EditText setelah berhasil memperbarui password
                binding.newPasswordEditText.text?.clear() // Mengosongkan EditText setelah berhasil memperbarui password
                binding.confirmPasswordEditText.text?.clear() // Mengosongkan EditText setelah berhasil memperbarui password
            } else {
                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
            }
        })
    }

}