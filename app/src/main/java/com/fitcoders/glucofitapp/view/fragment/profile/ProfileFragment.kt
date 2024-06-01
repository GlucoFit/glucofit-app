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
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityLoginBinding
import com.fitcoders.glucofitapp.databinding.FragmentProfileBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.login.LoginActivity
import com.fitcoders.glucofitapp.view.activity.login.LoginViewModel
import com.fitcoders.glucofitapp.view.activity.register.RegisterActivity
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

        // Optionally clear preferences and navigate to login in ViewModel or handle here
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

   /* private fun showAlertDialog() {
        val TAG = "AlertDialog"
        val builder = AlertDialog.Builder(requireContext())
        val alert = builder.create()
        builder
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.sure))
            .setPositiveButton(getString(R.string.No)) { _, _ ->
                Log.d(TAG, "Positive button (No) clicked")
                alert.cancel()
            }
            .setNegativeButton(getString(R.string.Yes)) { _, _ ->
                Log.d(TAG, "Negative button (Yes) clicked")
                profileViewModel.logout()
            }
            .show()
        Log.d(TAG, "AlertDialog shown")
    }*/

    companion object {
        fun newInstance() = ProfileFragment()
    }
}


