package com.fitcoders.glucofitapp.view.activity.profile.account

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityAccountBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.login.LoginActivity

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var modelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel menggunakan ViewModelFactory
        modelFactory = ViewModelFactory.getInstance(this)
        accountViewModel = ViewModelProvider(this, modelFactory).get(AccountViewModel::class.java)

        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        val titleText: TextView = findViewById(R.id.titleText)
        val backButton: ImageButton = findViewById(R.id.backButton)

        titleText.text = "Account"
        backButton.visibility = ImageButton.VISIBLE

        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }

        // Mengatur click listener untuk tombol update username
        binding.updateUsernameButton.setOnClickListener {
            val newUsername = binding.usernameEditText.text.toString().trim()
            if (newUsername.isNotEmpty()) {
                accountViewModel.updateUsername(newUsername)
            } else {
                Toast.makeText(this, "Please enter a new username", Toast.LENGTH_SHORT).show()
            }
        }

        // Mengatur click listener untuk tombol update email
        binding.updateEmailButton.setOnClickListener {
            val newEmail = binding.emailEditText.text.toString().trim()
            if (newEmail.isNotEmpty()) {
                accountViewModel.updateEmail(newEmail)
            } else {
                Toast.makeText(this, "Please enter a new email", Toast.LENGTH_SHORT).show()
            }
        }

        // Mengatur click listener untuk tombol hapus akun
        binding.deleteAccountButton.setOnClickListener {
            accountViewModel.deleteUser()
        }
    }

    private fun setupObservers() {
        // Mengamati perubahan dalam update username response
        accountViewModel.updateUsernameResponse.observe(this, Observer { response ->
            if (response != null && response.userName != null) {
                Toast.makeText(this, "Username updated to ${response.userName}", Toast.LENGTH_SHORT).show()
                binding.usernameEditText.text?.clear() // Mengosongkan EditText setelah berhasil memperbarui username
            } else {
                Toast.makeText(this, "Failed to update username", Toast.LENGTH_SHORT).show()
            }
        })

        // Mengamati perubahan dalam update email response
        accountViewModel.updateEmailResponse.observe(this, Observer { response ->
            if (response != null && response.email != null) {
                Toast.makeText(this, "Email updated to ${response.email}", Toast.LENGTH_SHORT).show()
                binding.emailEditText.text?.clear() // Mengosongkan EditText setelah berhasil memperbarui email
            } else {
                Toast.makeText(this, "Failed to update email", Toast.LENGTH_SHORT).show()
            }
        })

        // Mengamati perubahan dalam delete user response
        accountViewModel.deleteUserResponse.observe(this, Observer { response ->
            if (response != null) {
                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                navigateToLogin() // Panggil metode untuk membuka halaman login
            } else {
                Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Menutup aktivitas saat ini
    }
}
