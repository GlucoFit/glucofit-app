package com.fitcoders.glucofitapp.view.fragment.profile

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // Observe the session for changes and update UI accordingly
         profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
             binding.email.text = user.email
             binding.username.text = user.username
         }

        // Setting click listener for logout button
        binding.btnLogout.setOnClickListener {
            showAlertDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Clear the binding when the view is destroyed to avoid memory leaks
    }

    private fun showAlertDialog() = AlertDialog.Builder(requireContext())
        .setTitle(R.string.logout)
        .setMessage(R.string.sure)
        .setPositiveButton(R.string.No) { dialog, _ -> dialog.dismiss() } // Use dismiss to just close the dialog
        .setNegativeButton(R.string.Yes) { _, _ -> this.performLogout() } // Handle logout inside performLogout method
        .create()
        .show()

    private fun performLogout() {
        profileViewModel.logout() // Delegate logout to ViewModel
        Toast.makeText(requireContext(), "Logout successful", Toast.LENGTH_SHORT).show()

    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}


