package com.fitcoders.glucofitapp.view.fragment.profile

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.UserPreference
import com.fitcoders.glucofitapp.databinding.ActivityLoginBinding
import com.fitcoders.glucofitapp.databinding.FragmentProfileBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.login.LoginActivity
import com.fitcoders.glucofitapp.view.activity.login.LoginViewModel
import com.fitcoders.glucofitapp.view.activity.register.RegisterActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log
import android.content.Intent
import android.os.Bundle
import com.fitcoders.glucofitapp.view.activity.profile.account.AccountActivity
import com.fitcoders.glucofitapp.view.activity.profile.password.PasswordActivity
import com.fitcoders.glucofitapp.view.activity.profile.selfassessmentresult.SelfAssessmentResultActivity


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var modelFactory: ViewModelFactory
    private val profileViewModel: ProfileViewModel by viewModels { modelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelFactory = ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleText: TextView = binding.root.findViewById(R.id.titleText)
        val backButton: ImageButton = binding.root.findViewById(R.id.backButton)

        titleText.text = "Profile"
        backButton.visibility = View.GONE

        observeViewModel()
        // Panggil fetchUserData dari ViewModel untuk mengambil data pengguna saat fragment dimuat
        profileViewModel.fetchUserData()

        // Setting click listener for logout button
        binding.btnLogout.setOnClickListener {
            showAlertDialog()
        }

        binding.account.setOnClickListener {
            val intent = Intent(requireContext(), AccountActivity::class.java)
            startActivity(intent)
        }

        binding.password.setOnClickListener {
            val intent = Intent(requireContext(), PasswordActivity::class.java)
            startActivity(intent)
        }

        binding.selfAssessmentResult.setOnClickListener {
            val intent = Intent(requireContext(), SelfAssessmentResultActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Clear the binding when the view is destroyed to avoid memory leaks
    }

    private fun observeViewModel() {
        profileViewModel.userResponse.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.email.text = user.email
                binding.username.text = user.userName
            }
        }

    }

    private fun showAlertDialog() = AlertDialog.Builder(requireContext())
        .setTitle(R.string.logout)
        .setMessage(R.string.sure)
        .setPositiveButton(R.string.No) { dialog, _ -> dialog.dismiss() } // Use dismiss to just close the dialog
        .setNegativeButton(R.string.Yes) { _, _ -> this.performLogout() } // Handle logout inside performLogout method
        .create()
        .show()

    private fun performLogout() {
        // Memanggil metode logout di ProfileViewModel dan mengelola hasilnya
        profileViewModel.logout { success, message ->
            lifecycleScope.launch {
                if (success) {
                    Toast.makeText(requireContext(), "Logout successful", Toast.LENGTH_SHORT).show()
                    // Pindah ke layar login atau layar yang sesuai setelah logout
                    navigateToLoginScreen()
                } else {
                    Toast.makeText(requireContext(), message ?: "Logout failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToLoginScreen() {
        // Implementasikan navigasi ke layar login atau layar lain yang sesuai setelah logout
        // Misalnya:
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }



    companion object {
        fun newInstance() = ProfileFragment()
    }
}


