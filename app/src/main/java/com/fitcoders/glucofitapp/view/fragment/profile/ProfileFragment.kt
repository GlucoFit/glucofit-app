package com.fitcoders.glucofitapp.view.fragment.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivityLoginBinding
import com.fitcoders.glucofitapp.databinding.FragmentProfileBinding
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.register.RegisterActivity
import com.fitcoders.glucofitapp.view.fragment.history.HistoryFragment
import com.fitcoders.glucofitapp.view.fragment.history.HistoryViewModel


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var modelFactory: ViewModelFactory
    private val profileViewModel: ProfileViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // modelFactory = ViewModelFactory.getInstance(this)

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

        // Set click listener for logout button
        binding.btnLogout.setOnClickListener {
            showAlertDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.sure))
            .setPositiveButton(getString(R.string.No)) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(getString(R.string.Yes)) { _, _ ->
                profileViewModel.logout()
            }
            .create()
            .show()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }

}
